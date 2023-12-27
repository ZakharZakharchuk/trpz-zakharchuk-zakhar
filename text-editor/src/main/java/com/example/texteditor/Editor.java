package com.example.texteditor;

import com.example.texteditor.command.Command;
import com.example.texteditor.command.CommandHistory;
import com.example.texteditor.command.CommentUncommentCommand;
import com.example.texteditor.command.CopyCommand;
import com.example.texteditor.command.CreateSnippetCommand;
import com.example.texteditor.command.CutCommand;
import com.example.texteditor.command.FindAndReplaceCommand;
import com.example.texteditor.command.OpenFileCommand;
import com.example.texteditor.command.PasteCommand;
import com.example.texteditor.command.RemoveBookmarkCommand;
import com.example.texteditor.command.SaveFileCommand;
import com.example.texteditor.command.SetBookmarkCommand;
import com.example.texteditor.command.ShowSnippetCommand;
import com.example.texteditor.observer.ObserverManager;
import com.example.texteditor.observer.SyntaxHighlightListener;
import com.example.texteditor.repository.SnippetRepository;
import com.example.texteditor.service.HelpService;
import com.example.texteditor.service.LineNumberService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Editor {

    private JComboBox<String> autoCompleteComboBox;
    private List<String> autoCompleteList;

    public JTextPane textPane;
    public String clipboard;
    private final ObserverManager observerManager=new ObserverManager();
    private final CommandHistory history = new CommandHistory();
    private final JFrame frame = new JFrame("Text editor (type & use buttons, Luke!)");
    public final List<Integer> bookmarks = new ArrayList<>();
    public LineNumberService lineNumbers;

    public Editor() {
        new SyntaxHighlightListener(this, observerManager);
        initAutoCompleteList();
        init();
    }

    private void initAutoCompleteList() {
        // Initialize your list of autocomplete suggestions
        autoCompleteList = new ArrayList<>();
        autoCompleteList.add("class");
        autoCompleteList.add("public");
        autoCompleteList.add("void");
        autoCompleteList.add("new");

    }

    public void init() {
        JPanel content = new JPanel();

        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        textPane = new JTextPane();
        content.add(textPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu snippetMenu = new JMenu("Snippets");
        JMenu helpMenu = new JMenu("Help");
        JMenu bookmarkMenu = new JMenu("Bookmarks");

        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");

        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem undo = new JMenuItem("Undo");

        JMenuItem commentUncommentItem = new JMenuItem("Comment/Uncomment Block");
        JMenuItem findReplaceItem = new JMenuItem("Find and Replace");

        JMenuItem createSnippetMenuItem = new JMenuItem("New Snippet");
        JMenuItem showSnippetMenuItem = new JMenuItem("Show Snippets");
        JMenuItem helpMenuItem = new JMenuItem("Show help");

        JMenuItem setBookmarkMenuItem = new JMenuItem("Set Bookmark");
        JMenuItem removeBookmarkMenuItem = new JMenuItem("Remove Bookmark");

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);

        editMenu.add(commentUncommentItem);
        editMenu.add(findReplaceItem);
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(undo);

        helpMenu.add(helpMenuItem);

        snippetMenu.add(createSnippetMenuItem);
        snippetMenu.add(showSnippetMenuItem);

        bookmarkMenu.add(setBookmarkMenuItem);
        bookmarkMenu.add(removeBookmarkMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(snippetMenu);
        menuBar.add(helpMenu);
        menuBar.add(bookmarkMenu);
        helpMenuItem.addActionListener(e -> {
            try {
                new HelpService().openNewWindow("http://localhost:8080/help");
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.out.println("Error when call API");
            }
        });

        lineNumbers = new LineNumberService(textPane, bookmarks);
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setRowHeaderView(lineNumbers);
        frame.add(scrollPane);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        setBookmarkMenuItem.addActionListener(e -> executeCommand(new SetBookmarkCommand(this)));
        removeBookmarkMenuItem.addActionListener(
              e -> executeCommand(new RemoveBookmarkCommand(this)));

        copy.addActionListener(e -> executeCommand(new CopyCommand(this)));
        cut.addActionListener(e -> executeCommand(new CutCommand(this)));
        paste.addActionListener(e -> executeCommand(new PasteCommand(this)));
        undo.addActionListener(e -> undo());

        frame.setJMenuBar(menuBar);
        frame.setSize(450, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        openMenuItem.addActionListener(e -> executeCommand(new OpenFileCommand(this)));
        saveMenuItem.addActionListener(e -> executeCommand(new SaveFileCommand(this)));

        commentUncommentItem.addActionListener(
              e -> executeCommand(new CommentUncommentCommand(this)));
        findReplaceItem.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog("Enter text to find:");
            String replaceText = JOptionPane.showInputDialog("Enter replacement text:");
            executeCommand(new FindAndReplaceCommand(this, searchText, replaceText));
        });

        createSnippetMenuItem.addActionListener(e ->
              executeCommand(new CreateSnippetCommand(this, new SnippetRepository()))
        );
        showSnippetMenuItem.addActionListener(e ->
              executeCommand(new ShowSnippetCommand(this, new SnippetRepository())));

        initAutoCompleteComboBox();
        textPane.addKeyListener(createKeyListener());
        frame.getContentPane().add(autoCompleteComboBox, BorderLayout.SOUTH);
    }

    private void initAutoCompleteComboBox() {
        autoCompleteComboBox = new JComboBox<>(autoCompleteList.toArray(new String[0]));
        autoCompleteComboBox.setEditable(true);
        autoCompleteComboBox.setSelectedItem(null);


        autoCompleteComboBox.addActionListener(e -> {
            if (autoCompleteComboBox.getSelectedItem() != null) {
                insertAutoCompleteText((String) autoCompleteComboBox.getSelectedItem());
            }
        });

        frame.getContentPane().add(autoCompleteComboBox, BorderLayout.NORTH);
    }

    private void executeCommand(Command command) {
        if (command.execute()) {
            history.push(command);
        }
    }

    private void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            if (command != null) {
                command.undo();
            }
        }
    }

    private KeyListener createKeyListener() {
        return new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                char typedChar = e.getKeyChar();
                if (Character.isLetterOrDigit(typedChar)) {
                    showAutoCompleteDropdown();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                observerManager.notifyObservers();
            }
        };
    }

    private void showAutoCompleteDropdown() {
        List<String> filteredList = new ArrayList<>();
        for (String suggestion : autoCompleteList) {
            if (suggestion.contains(getCurrentWord())) {
                filteredList.add(suggestion);
            }
        }
        autoCompleteComboBox.removeAll();
        if (filteredList.isEmpty()) {
            autoCompleteComboBox.setPopupVisible(false);
        } else {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
                  filteredList.toArray(new String[0]));
            autoCompleteComboBox.setModel(model);

            autoCompleteComboBox.setPopupVisible(true);
        }
    }

    private String getCurrentWord() {
        String content = textPane.getText();
        int caretPosition = textPane.getCaretPosition();
        int start = caretPosition - 1;

        while (start >= 0 && Character.isLetterOrDigit(content.charAt(start))) {
            start--;
        }

        return content.substring(start + 1, caretPosition);
    }

    private void insertAutoCompleteText(String suggestion) {
        String currentWord = getCurrentWord();
        int caretPosition = textPane.getCaretPosition();
        int start = caretPosition - currentWord.length();
        textPane.select(start, caretPosition);
        textPane.replaceSelection(suggestion);
        autoCompleteComboBox.setPopupVisible(false);
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}

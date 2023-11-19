package com.example.texteditor;

import com.example.texteditor.command.Command;
import com.example.texteditor.command.CommandHistory;
import com.example.texteditor.command.CommentUncommentCommand;
import com.example.texteditor.command.CopyCommand;
import com.example.texteditor.command.CutCommand;
import com.example.texteditor.command.FindAndReplaceCommand;
import com.example.texteditor.command.OpenFileCommand;
import com.example.texteditor.command.PasteCommand;
import com.example.texteditor.command.RemoveBookmarkCommand;
import com.example.texteditor.command.SaveFileCommand;
import com.example.texteditor.command.SetBookmarkCommand;
import com.example.texteditor.observer.ObserverManager;
import com.example.texteditor.service.HelpService;
import com.example.texteditor.service.LineNumberService;
import com.example.texteditor.service.SnippetService;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Editor {

    public JTextPane textPane;
    public String clipboard;
    private final ObserverManager observerManager;
    private final CommandHistory history = new CommandHistory();
    private final JFrame frame = new JFrame("Text editor (type & use buttons, Luke!)");
    public final List<Integer> bookmarks = new ArrayList<>();
    public LineNumberService lineNumbers;

    public Editor(ObserverManager observerManager) {
        this.observerManager = observerManager;
    }

    public void init() {
        JPanel content = new JPanel();
        JPanel buttonPanel = new JPanel();

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

        JButton setBookmarkButton = new JButton("Set Bookmark");
        JButton removeBookmarkButton = new JButton("Remove Bookmark");

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

        buttonPanel.add(setBookmarkButton);
        buttonPanel.add(removeBookmarkButton);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(snippetMenu);
        menuBar.add(helpMenu);
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
        frame.add(buttonPanel, BorderLayout.SOUTH);

        setBookmarkButton.addActionListener(e -> executeCommand(new SetBookmarkCommand(this)));
        removeBookmarkButton.addActionListener(
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
              new SnippetService().createSnippetDialog(frame)
        );
        showSnippetMenuItem.addActionListener(e ->
              new SnippetService().showSnippetDialog(frame, textPane.getStyledDocument()));

        textPane.addKeyListener(createKeyListener());
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
}

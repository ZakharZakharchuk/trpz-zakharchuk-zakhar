package com.example.texteditor;

import com.example.texteditor.command.Command;
import com.example.texteditor.command.CommandHistory;
import com.example.texteditor.command.CopyCommand;
import com.example.texteditor.command.CutCommand;
import com.example.texteditor.command.OpenFileCommand;
import com.example.texteditor.command.PasteCommand;
import com.example.texteditor.observer.Observer;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.Element;

public class Editor {

    public JTextPane textPane;
    public String clipboard;

    private final CommandHistory history = new CommandHistory();
    JFrame frame = new JFrame("Text editor (type & use buttons, Luke!)");
    List<Integer> bookmarks = new ArrayList<>();
    LineNumberPanel lineNumbers;
    private final List<Observer> observers = new ArrayList<>();


    public void init() {
        JPanel content = new JPanel();
        JPanel buttonPanel = new JPanel();

        frame.setContentPane(content);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        textPane = new JTextPane();
        content.add(textPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu snippetMenu = new JMenu("Snippets");

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

        snippetMenu.add(createSnippetMenuItem);
        snippetMenu.add(showSnippetMenuItem);

        buttonPanel.add(setBookmarkButton);
        buttonPanel.add(removeBookmarkButton);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(snippetMenu);

        lineNumbers = new LineNumberPanel(textPane, bookmarks);
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setRowHeaderView(lineNumbers);
        frame.add(scrollPane);

        Editor editor = this;

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        setBookmarkButton.addActionListener(e -> setBookmark());
        removeBookmarkButton.addActionListener(e -> removeBookmark());

        copy.addActionListener(e -> executeCommand(new CopyCommand(editor)));
        cut.addActionListener(e -> executeCommand(new CutCommand(editor)));
        paste.addActionListener(e -> executeCommand(new PasteCommand(editor)));
        undo.addActionListener(e -> undo());

        frame.setJMenuBar(menuBar);
        frame.setSize(450, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        openMenuItem.addActionListener(e -> openFile());
        saveMenuItem.addActionListener(e -> saveFile());

        commentUncommentItem.addActionListener(e -> commentUncommentBlock());
        findReplaceItem.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog("Enter text to find:");
            String replaceText = JOptionPane.showInputDialog("Enter replacement text:");
            findAndReplace(searchText, replaceText);
        });

        createSnippetMenuItem.addActionListener(e ->
              new SnippetEditor().createSnippetDialog(frame)
        );

        textPane.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                notifyObservers();
            }
        });
    }


    private void executeCommand(Command command) {
        if (command.execute()) {
            history.push(command);
        }
    }

    private void undo() {
        if (history.isEmpty()) {
            return;
        }

        Command command = history.pop();
        if (command != null) {
            command.undo();
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    private void setBookmark() {
        int caretPosition = textPane.getCaretPosition();
        Element root = textPane.getDocument().getDefaultRootElement();
        int lineNumber = root.getElementIndex(caretPosition) + 1;

        if (!bookmarks.contains(lineNumber)) {
            bookmarks.add(lineNumber);
            lineNumbers.updateLineNumbers();
            JOptionPane.showMessageDialog(null, "Bookmark set at line " + lineNumber);
        } else {
            JOptionPane.showMessageDialog(null, "Bookmark already exists at line " + lineNumber);
        }
    }

    // Method to remove a bookmark
    private void removeBookmark() {
        int caretPosition = textPane.getCaretPosition();
        Element root = textPane.getDocument().getDefaultRootElement();
        int lineNumber = root.getElementIndex(caretPosition) + 1;

        if (bookmarks.contains(lineNumber)) {
            bookmarks.remove(Integer.valueOf(lineNumber));
            lineNumbers.updateLineNumbers();
            JOptionPane.showMessageDialog(null, "Bookmark removed from line " + lineNumber);
        } else {
            JOptionPane.showMessageDialog(null, "No bookmark found at line " + lineNumber);
        }
    }

    public void commentUncommentBlock() {
        String selectedText = textPane.getSelectedText();
        if (selectedText != null) {
            String[] lines = selectedText.split("\n");
            StringBuilder modifiedText = new StringBuilder();
            boolean isCommented = false;

            for (String line : lines) {
                if (line.trim().startsWith("//")) {
                    // Uncomment the line
                    modifiedText.append(line.replaceFirst("//", "")).append("\n");
                    isCommented = true;
                } else {
                    // Comment the line
                    modifiedText.append("//").append(line).append("\n");
                }
            }

            textPane.replaceSelection(modifiedText.toString().trim());
            if (isCommented) {
                textPane.setCaretPosition(textPane.getCaretPosition() - modifiedText.length());
            }
        }
    }

    // Macro 3: Find and Replace
    public void findAndReplace(String searchText, String replaceText) {
        String text = textPane.getText();
        text = text.replace(searchText, replaceText);
        textPane.setText(text);
    }

    private byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        return bytes;
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getPath();
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(textPane.getText());
                writer.close();
                JOptionPane.showMessageDialog(null, "Document saved successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving the document.");
            }
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String encoding = new InputStreamReader(
                      new FileInputStream(selectedFile)).getEncoding();
                byte[] fileBytes = readBytesFromFile(selectedFile);
                new OpenFileCommand(this).execute(fileBytes, encoding);
                notifyObservers();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

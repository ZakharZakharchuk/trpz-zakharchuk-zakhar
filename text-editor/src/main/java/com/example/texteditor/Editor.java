package com.example.texteditor;

import com.example.texteditor.command.Command;
import com.example.texteditor.command.CommandHistory;
import com.example.texteditor.command.CopyCommand;
import com.example.texteditor.command.CutCommand;
import com.example.texteditor.command.MacroCommand;
import com.example.texteditor.command.PasteCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import org.springframework.util.ResourceUtils;

public class Editor {

    public JTextPane textPane;
    public String clipboard;

    private final CommandHistory history = new CommandHistory();
    StyledDocument styledDocument;
    private static List<MacroCommand> recordedCommands = new ArrayList<>();
    private static boolean recording = false;
    private static List<ActionEvent> recordedActions = new ArrayList<>();
    private static List<String> recordedTextEdits = new ArrayList<>();

    public ActionListener getActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the command from the selected menu item
                String menuItemName = e.getActionCommand();

                // Call the method to record menu action
                recordMenuAction(menuItemName);
            }
        };
    }

    public void init() {
        JFrame frame = new JFrame("Text editor (type & use buttons, Luke!)");
        JPanel content = new JPanel();
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        textPane = new JTextPane();
        content.add(textPane);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton ctrlC = new JButton("Ctrl+C");
        JButton ctrlX = new JButton("Ctrl+X");
        JButton ctrlV = new JButton("Ctrl+V");
        JButton ctrlZ = new JButton("Ctrl+Z");
        Editor editor = this;

        JMenuBar menuBar = new JMenuBar();
        JMenu snippetMenu = new JMenu("Snippet");
        JMenuItem showSnippetMenuItem = new JMenuItem("Show Snippets");
        JMenuItem createSnippetMenuItem = new JMenuItem("New Snippet");
        snippetMenu.add(createSnippetMenuItem);

        JMenu macroMenu = new JMenu("Macro");
        menuBar.add(macroMenu);

        JMenuItem startRecordingItem = new JMenuItem("Start Recording");
        JMenuItem stopRecordingItem = new JMenuItem("Stop Recording");
        JMenuItem playbackItem = new JMenuItem("Playback");
        JMenuItem saveMacroItem = new JMenuItem("Save Macro");
        JMenuItem loadMacroItem = new JMenuItem("Load Macro");

        macroMenu.add(startRecordingItem);
        macroMenu.add(stopRecordingItem);
        macroMenu.add(playbackItem);

        macroMenu.add(startRecordingItem);
        macroMenu.add(stopRecordingItem);
        macroMenu.add(playbackItem);
        macroMenu.add(saveMacroItem);
        macroMenu.add(loadMacroItem);

        menuBar.add(macroMenu);
        frame.setJMenuBar(menuBar);

        startRecordingItem.addActionListener(e -> startRecording());
        stopRecordingItem.addActionListener(e -> stopRecording());
        playbackItem.addActionListener(e -> playback());
        saveMacroItem.addActionListener(e -> saveMacro());
        loadMacroItem.addActionListener(e -> loadMacro());
        // Add a document listener to the text pane to capture text editing actions

        ActionListener menuActionListener = getActionListener();
        ctrlC.addActionListener(menuActionListener);
        ctrlV.addActionListener(menuActionListener);
        ctrlX.addActionListener(menuActionListener);
        ctrlZ.addActionListener(menuActionListener);
        showSnippetMenuItem.addActionListener(menuActionListener);
        createSnippetMenuItem.addActionListener(menuActionListener);

        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                recordTextEdit(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Handle text removal if needed
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Handle attribute changes if needed
            }
        });

        showSnippetMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SnipetEditor().showSnippetDialog(frame, textPane.getStyledDocument());
            }
        });
        createSnippetMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SnipetEditor().createSnippetDialog(frame);
            }
        });

        snippetMenu.add(showSnippetMenuItem);
        menuBar.add(snippetMenu);

        styledDocument = textPane.getStyledDocument();
        textPane.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyTyped(KeyEvent e) {
                highlightSyntax(0, textPane.getText().length());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        ctrlC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new CopyCommand(editor));
            }
        });
        ctrlX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new CutCommand(editor));
            }
        });
        ctrlV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new PasteCommand(editor));
            }
        });
        ctrlZ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        frame.setJMenuBar(menuBar);
        buttons.add(ctrlC);
        buttons.add(ctrlX);
        buttons.add(ctrlV);
        buttons.add(ctrlZ);
        content.add(buttons);
        frame.setSize(450, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method to start recording
    private void startRecording() {
        recording = true;
        recordedActions.clear();
        recordedTextEdits.clear();
    }

    // Method to stop recording
    private void stopRecording() {
        recording = false;
    }

    // Method to playback recorded actions
    private void playback() {
        if (recordedActions.isEmpty() && recordedTextEdits.isEmpty()) {
            return;
        }

        StyledDocument doc = textPane.getStyledDocument();
        for (ActionEvent action : recordedActions) {
            actionPerformed(action);
        }

        // Simulate recorded text edits
        for (String textEdit : recordedTextEdits) {
            try {
                doc.insertString(doc.getLength(), textEdit, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to save recorded macro to a JSON file
    private void saveMacro() {
        try {
            System.out.println(recordedActions);
            System.out.println(recordedTextEdits);
            List<String> serializedActions = new ArrayList<>();
            for (ActionEvent action : recordedActions) {
                serializedActions.add(action.getActionCommand());
            }
            for (String str : recordedTextEdits) {
                serializedActions.add(str);
            }
            String json = String.join(",", serializedActions);
            System.out.println(json);
            new ObjectMapper().writeValue(new File("src/main/resources/macros.json"), json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Method to load a recorded macro from a JSON file
    private void loadMacro() {
        try {
            String json = new ObjectMapper().readValue(
                  ResourceUtils.getFile("src/main/resources/macros.json"),
                  String.class);
            System.out.println(json);
            String[] actions = json.split(",");
            for (String action : actions) {
                recordedActions.add(
                      new ActionEvent(new JTextPane(), ActionEvent.ACTION_PERFORMED,
                            action));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to record a text editing action
    private void recordTextEdit(DocumentEvent e) {
        if (recording) {
            try {
                Document doc = e.getDocument();
                int offset = e.getOffset();
                int length = e.getLength();
                recordedTextEdits.add(doc.getText(offset, length));
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void actionPerformed(ActionEvent e) {
        StyledDocument doc = textPane.getStyledDocument();
        String command = e.getActionCommand();

        if ("Insert Text".equals(command)) {
            try {
                doc.insertString(doc.getLength(), e.getActionCommand(), null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to record a menu action
    private void recordMenuAction(String menuItemName) {
        if (recording) {
            recordedActions.add(
                  new ActionEvent(new JMenuItem(menuItemName), ActionEvent.ACTION_PERFORMED,
                        menuItemName));
        }
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

    private void highlightSyntax(int start, int length) {
        new SyntaxHighlight().getAttributesForText(this, styledDocument, start, length);
    }
}

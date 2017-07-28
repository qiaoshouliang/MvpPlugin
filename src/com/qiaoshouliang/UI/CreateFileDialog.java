package com.qiaoshouliang.UI;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.qiaoshouliang.CreateFile.CreateFile;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateFileDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField;

    public CreateFileDialog(AnActionEvent e) {

        setTitle("New Mvp File");
        setContentPane(contentPane);
        setModal(true);
        setMinimumSize(new Dimension(260, 120));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e1 -> onOK(e));

        buttonCancel.addActionListener(e1 -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e1 -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(AnActionEvent e) {
        if (TextUtils.isEmpty(textField.getText())) {
            Messages.showErrorDialog("Generation failed, " +
                            "your must enter class name",
                    "Class Name is null");
        } else {
            new CreateFile(e,textField.getText()).execute();
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }


}

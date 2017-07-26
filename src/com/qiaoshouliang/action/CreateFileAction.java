package com.qiaoshouliang.action;

import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.qiaoshouliang.UI.CreateFileDialog;

/**
 * Created by qiaoshouliang on 17/7/25.
 */
public class CreateFileAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        e.getPresentation().setIcon(IconLoader.getIcon("/icons/icon_tf.png"));
        new CreateFileDialog(e).setVisible(true);
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);

        IdeView ideView = e.getRequiredData(LangDataKeys.IDE_VIEW);
        PsiDirectory directory = ideView.getOrChooseDirectory();
        if (directory.getName().equals("contract"))
            e.getPresentation().setEnabledAndVisible(true);
        else
            e.getPresentation().setEnabledAndVisible(false);

    }
}

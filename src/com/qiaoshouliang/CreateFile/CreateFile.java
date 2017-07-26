package com.qiaoshouliang.CreateFile;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;

/**
 * Created by qiaoshouliang on 17/7/26.
 */
public class CreateFile {
    public void createMVPFile(String className, Project project, PsiDirectory directory, JavaDirectoryService directoryService, PsiElementFactory factory) {

        WriteCommandAction.runWriteCommandAction(project, new Runnable() {

            @Override
            public void run() {

                PsiDirectory PDirectory, MDirectory;
                PsiClass basePresenter, baseView;

//                if (!directory.getName().endsWith("contract")){
//                    Messages.showErrorDialog(
//                            "Generation failed, " +
//                                    "you must create contract directory first",
//                            "Directory Error");
//                    return;
//                }
                PDirectory = getDirectoryByName("presenter", directory);
                MDirectory = getDirectoryByName("model", directory);
                if (PDirectory == null) {
                    PDirectory = directory.getParentDirectory().createSubdirectory("presenter");
                }

                if (MDirectory == null) {
                    MDirectory = directory.getParentDirectory().createSubdirectory("model");
                }


                PsiFile basePresenterPsiFile = directory.getParentDirectory().findFile("BasePresenter");
                if (basePresenterPsiFile != null) {
                    basePresenterPsiFile.delete();
                }
                basePresenter = directoryService.createClass(directory.getParentDirectory(), "BasePresenter", "BasePresenter");

                PsiFile baseViewPsiFile = directory.getParentDirectory().findFile("BaseView");
                if (baseViewPsiFile != null) {
                    baseViewPsiFile.delete();
                }
                baseView = directoryService.createClass(directory.getParentDirectory(), "BaseView", "BaseView");


                PsiClass contract = directoryService.createClass(directory, className, "Contract");
                PsiClass model = directoryService.createClass(MDirectory, className, "Model");
                PsiClass presenter = directoryService.createClass(PDirectory, className, "Presenter");


                PsiImportStatement importBasePresenter = factory.createImportStatement(basePresenter);
                PsiImportStatement importBaseView = factory.createImportStatement(baseView);
                PsiImportStatement importContract = factory.createImportStatement(contract);
                PsiImportStatement importModel = factory.createImportStatement(model);

                ((PsiJavaFile) contract.getContainingFile()).getImportList().add(importBasePresenter);
                ((PsiJavaFile) contract.getContainingFile()).getImportList().add(importBaseView);
                //格式化代码
                CodeStyleManager.getInstance(project).reformat(contract);
                ((PsiJavaFile) model.getContainingFile()).getImportList().add(importContract);

                ((PsiJavaFile) presenter.getContainingFile()).getImportList().add(importContract);
                ((PsiJavaFile) presenter.getContainingFile()).getImportList().add(importModel);


            }
        });
    }


    public PsiDirectory getDirectoryByName(String name, PsiDirectory directory) {


        return directory.getParentDirectory().findSubdirectory(name);
    }
}

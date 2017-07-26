package com.qiaoshouliang.CreateFile;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

/**
 * Created by qiaoshouliang on 17/7/26.
 */
public class CreateFile {


    public void createMVPFile(String className, Project project, PsiDirectory cDirectory, JavaDirectoryService directoryService, PsiElementFactory factory) {
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {

            @Override
            public void run() {

                PsiDirectory pDirectory, mDirectory;
                PsiClass basePresenter, baseView;

                /**
                 * 创建presenter目录和contract目录
                 */
                pDirectory = getDirectoryByName("presenter", cDirectory);
                mDirectory = getDirectoryByName("model", cDirectory);
                if (pDirectory == null) {
                    pDirectory = cDirectory.getParentDirectory().createSubdirectory("presenter");
                }
                if (mDirectory == null) {
                    mDirectory = cDirectory.getParentDirectory().createSubdirectory("model");
                }
                /**
                 * 创建BasePresenter和BaseView
                 */
                PsiFile basePresenterPsiFile = cDirectory.getParentDirectory().findFile("BasePresenter.java");
                if (basePresenterPsiFile == null) {
//                    basePresenterPsiFile.delete();
                    basePresenter = directoryService.createClass(cDirectory.getParentDirectory(), "BasePresenter", "BasePresenter");
                } else {
                    basePresenter = getPsiClassByName(project, "BasePresenter", basePresenterPsiFile);
                }

                PsiFile baseViewPsiFile = cDirectory.getParentDirectory().findFile("BaseView.java");
                if (baseViewPsiFile == null) {
                    baseView = directoryService.createClass(cDirectory.getParentDirectory(), "BaseView", "BaseView");
                } else {
                    baseView = getPsiClassByName(project, "BaseView", baseViewPsiFile);
                }
                /**
                 * 判断该类是否已经建立过了
                 */
                if (cDirectory.findFile("I" + className + "Contract.java") != null
                        || mDirectory.findFile(className + "ModelImpl.java") != null
                        || pDirectory.findFile(className + "PresenterImpl.java") != null) {
                    Messages.showErrorDialog("Generation failed, " +
                                    className + " already exists",
                            "File already exists");
                    return;
                }

                /**
                 * 创建 IExampleContract,ExampleModelImpl,ExamplePresenterImpl
                 */
                PsiClass contract = directoryService.createClass(cDirectory, className, "Contract");
                PsiClass model = directoryService.createClass(mDirectory, className, "Model");
                PsiClass presenter = directoryService.createClass(pDirectory, className, "Presenter");
                /**
                 * 创建 IExampleContract,ExampleModelImpl,ExamplePresenterImpl中需要的import
                 */

                PsiImportStatement importBasePresenter = factory.createImportStatement(basePresenter);
                PsiImportStatement importBaseView = factory.createImportStatement(baseView);
                PsiImportStatement importContract = factory.createImportStatement(contract);
                PsiImportStatement importModel = factory.createImportStatement(model);
                /**
                 * 为IExampleContract,ExampleModelImpl,ExamplePresenterImpl加入import
                 */
                ((PsiJavaFile) contract.getContainingFile()).getImportList().add(importBasePresenter);
                ((PsiJavaFile) contract.getContainingFile()).getImportList().add(importBaseView);

                ((PsiJavaFile) model.getContainingFile()).getImportList().add(importContract);

                ((PsiJavaFile) presenter.getContainingFile()).getImportList().add(importContract);
                ((PsiJavaFile) presenter.getContainingFile()).getImportList().add(importModel);


            }
        });
    }


    private PsiClass getPsiClassByName(Project project, String name, PsiFile basePresenterPsiFile) {
        PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        GlobalSearchScope search = GlobalSearchScope.fileScope(basePresenterPsiFile);

        PsiClass[] psiClasses = cache.getClassesByName(name, search);
        PsiClass psiClass = null;
        if (psiClasses.length != 0) {//if the class already exist.
            psiClass = psiClasses[0];
        }//and
        return psiClass;
    }

    public PsiDirectory getDirectoryByName(String name, PsiDirectory directory) {


        return directory.getParentDirectory().findSubdirectory(name);
    }
}

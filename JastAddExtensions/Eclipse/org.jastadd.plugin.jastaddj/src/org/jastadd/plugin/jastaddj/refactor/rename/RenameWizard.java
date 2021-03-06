package org.jastadd.plugin.jastaddj.refactor.rename;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

public class RenameWizard extends RefactoringWizard {
	public RenameWizard(RenameRefactoring refactoring, String pageTitle) {
		super(refactoring, DIALOG_BASED_USER_INTERFACE | PREVIEW_EXPAND_FIRST_NODE);
		setDefaultPageTitle(pageTitle);
	}

	protected void addUserInputPages() {
		RenameInputPage page= new RenameInputPage("Rename");
		addPage(page);
	}
}

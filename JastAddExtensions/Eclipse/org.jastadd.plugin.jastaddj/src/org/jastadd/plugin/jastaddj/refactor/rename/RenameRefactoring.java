package org.jastadd.plugin.jastaddj.refactor.rename;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ui.IEditorPart;
import org.jastadd.plugin.AST.IJastAddNode;
import org.jastadd.plugin.jastaddj.AST.IJastAddJRenameConditionNode;
import org.jastadd.plugin.model.JastAddModel;

import AST.MethodDecl;
import AST.TypeDecl;
import AST.Variable;

public class RenameRefactoring extends Refactoring {

	private JastAddModel model;
	private IJastAddNode selectedNode;
	private String name;
	private RefactoringStatus status;
	private Change changes;

	public RenameRefactoring(JastAddModel model, IEditorPart editorPart,
			IFile editorFile, ISelection selection, IJastAddNode selectedNode) {
		super();
		this.model = model;
		this.selectedNode = selectedNode;
	}

	public String getName() {
		return "Rename";
	}

	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		RefactoringStatus status = new RefactoringStatus();
		if(selectedNode instanceof TypeDecl
				|| selectedNode instanceof MethodDecl
				|| selectedNode instanceof Variable)
			/*OK*/;
		else
			status.addFatalError("Only types, methods, and variables can be renamed.");
		return status;
	}

	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		if(status != null)
			return status;
		status = new RefactoringStatus();
		if(selectedNode instanceof IJastAddJRenameConditionNode)
			changes = ((IJastAddJRenameConditionNode)selectedNode).checkRenameConditions(name, status, model);
		return status;
	}

	public void setName(String text) {
		name = text;
		status = null;
		changes = null;
	}

	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		return changes;
	}
}

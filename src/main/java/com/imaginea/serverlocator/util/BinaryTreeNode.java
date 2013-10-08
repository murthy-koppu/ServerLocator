package com.imaginea.serverlocator.util;

public class BinaryTreeNode {
	private int fromPort;
	private int toPort;
	private BinaryTreeNode leftChild;
	private BinaryTreeNode rightChild;

	public BinaryTreeNode(int fromPort, int toPort) {
		super();
		this.fromPort = fromPort;
		this.toPort = toPort;
	}

	public void addNode(int newFromPort, int newToPort){
		if(newFromPort < fromPort){
			
		}
	}
	
	
	
	public int getFromPort() {
		return fromPort;
	}

	public void setFromPort(int fromPort) {
		this.fromPort = fromPort;
	}

	public int getToPort() {
		return toPort;
	}

	public void setToPort(int toPort) {
		this.toPort = toPort;
	}

	public BinaryTreeNode getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(BinaryTreeNode leftChild) {
		this.leftChild = leftChild;
	}

	public BinaryTreeNode getRightChild() {
		return rightChild;
	}

	public void setRightChild(BinaryTreeNode rightChild) {
		this.rightChild = rightChild;
	}

}

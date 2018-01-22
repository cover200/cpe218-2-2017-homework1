import java.text.DecimalFormat;
import java.util.Stack;

import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;

public class Homework1 extends JPanel implements TreeSelectionListener {
	static Stack<Node> st = new Stack<Node>();

	private JEditorPane htmlPane;
	private JTree tree;

	Homework1(Node n) {
		super(new GridLayout(1,0));

		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode(n);
		createNodes(top);

		//Create a tree that allows one selection at a time.
		tree = new JTree(top);
		java.net.URL img = Homework1.class.getResource("middle.gif");
		ImageIcon icon = new ImageIcon(img);
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(icon);
		renderer.setClosedIcon(icon);
		tree.setCellRenderer(renderer);

		tree.getSelectionModel().setSelectionMode
				(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		tree.addTreeSelectionListener(this);

		tree.putClientProperty("JTree.lineStyle", "None");

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(tree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		//Add the split pane to this panel.
		add(splitPane);
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			String input = args[0];

			int i = -1, n = input.length();
			float ans;
			Node node , root = null;

			while (++i < n) {
				node = new Node(input.charAt(i));
				infix(node);
				root = node;
			}

			Node finalRoot = root;
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI(finalRoot);
				}
			});
		}
	} //end main

	public static void infix(Node n) {

		if(Character.isDigit(n.x))
			st.push(n);
		else {
			n.right = (Node)st.pop();
			n.left = (Node)st.pop();
			st.push(n);
		}
	} //end infix

	public static String inorder(Node n) {
		String tmp="";

	    if(!Character.isDigit(n.x) && n!=st.peek()) tmp+='(';
		if(n.left != null) tmp+=inorder(n.left);
		tmp+=n.x;
		if(n.right != null) tmp+=inorder(n.right);
        if(!Character.isDigit(n.x) && n!=st.peek()) tmp+=')';

        return tmp;
	} //end inorder

	public static float calculate(Node n) {
		switch(n.x) {
            case '+' : return calculate(n.left)+calculate(n.right);
            case '-' : return calculate(n.left)-calculate(n.right);
            case '*' : return calculate(n.left)*calculate(n.right);
            case '/' : return calculate(n.left)/calculate(n.right);
            default: return Character.getNumericValue(n.x);
		}
	} //end calculate

	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Node tmp = (Node)node.getUserObject();

		st.push(tmp);
		String text = inorder(tmp);
		if(text.length() > 1) {
			text += '=';
			float x = calculate(tmp);
			if(x == (int)x) text += (int)x;
			else text += x;
		}
		st.pop();

		htmlPane.setText(text);
	}

	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode tmp = null;
		Node node = (Node)top.getUserObject();

		if(node.left != null) {
			tmp = new DefaultMutableTreeNode(node.left);
			top.add(tmp);
			createNodes(tmp);
		}
		if(node.right != null) {
			tmp = new DefaultMutableTreeNode(node.right);
			top.add(tmp);
			createNodes(tmp);
		}
	}

	private static void createAndShowGUI(Node node) {
		//Create and set up the window.
		JFrame frame = new JFrame("Binary Tree Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new Homework1(node));

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
}

package dao;
import model.*;
// Node Class for BST
class JobNode {
    Job job;          // Job object
    JobNode left;     // Left child
    JobNode right;    // Right child

    public JobNode(Job job) {
        this.job = job;
        this.left = null;
        this.right = null;
    }
}

// Binary Search Tree Class for Job
public class JobBST {
    private class JobNode {
        Job job;
        JobNode left, right;

        JobNode(Job job) {
            this.job = job;
        }
    }

    private JobNode root;

    public void insert(Job job) {
        root = insertRecursive(root, job);
    }

    private JobNode insertRecursive(JobNode node, Job job) {
        if (node == null) return new JobNode(job);

        String newTitle = job.getTitle().toLowerCase();
        String nodeTitle = node.job.getTitle().toLowerCase();

        if (newTitle.compareTo(nodeTitle) < 0) {
            node.left = insertRecursive(node.left, job);
        } else {
            node.right = insertRecursive(node.right, job);
        }

        return node;
    }

    public Job search(String title) {
        return searchRecursive(root, title.toLowerCase());
    }

    private Job searchRecursive(JobNode node, String title) {
        if (node == null) return null;

        String nodeTitle = node.job.getTitle().toLowerCase();

        if (title.equals(nodeTitle)) return node.job;
        else if (title.compareTo(nodeTitle) < 0)
            return searchRecursive(node.left, title);
        else
            return searchRecursive(node.right, title);
    }
    public Job delete(String title) {
        root = deleteRecursive(root, title.toLowerCase());
        return null;
    }

    private JobNode deleteRecursive(JobNode root, String title) {
        if (root == null) return null;

        int cmp = title.compareTo(root.job.getTitle().toLowerCase());
        if (cmp < 0) {
            root.left = deleteRecursive(root.left, title);
        } else if (cmp > 0) {
            root.right = deleteRecursive(root.right, title);
        } else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            Job minLargerNode = findMin(root.right);
            root.job = minLargerNode;
            root.right = deleteRecursive(root.right, minLargerNode.getTitle().toLowerCase());
        }

        return root;
    }

    private Job findMin(JobNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.job;
    }

}

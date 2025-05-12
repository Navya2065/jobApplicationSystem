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
}

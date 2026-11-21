package model;

import java.util.ArrayList;

/**
 * BatchList - a Singly Linked List of Batch objects.
 * 
 * This is the main data structure we learned in class.
 * 
 * Structure:
 *   head -> Batch1 -> Batch2 -> Batch3 -> null
 * 
 * Operations we implement ourselves (instead of using ArrayList):
 *   - addFirst / addLast
 *   - find by batchId
 *   - remove by batchId
 *   - getAll (convert to ArrayList so the GUI can show it in a table)
 *   - nextId (generate the next batch ID like B10005)
 * 
 * Why we still use ArrayList sometimes:
 *   JTable and many Swing components work easier with ArrayList or arrays.
 *   So we keep the linked list as the "real" storage and convert when needed.
 */
public class BatchList {

    // The first node in the list. If the list is empty, head is null.
    private Batch head;

    // How many batches are currently in the list
    private int size;

    /**
     * Returns the first node (head) of the linked list.
     * Useful when we want to start walking through the list.
     */
    public Batch getHead() {
        return head;
    }

    /**
     * Returns how many batches are stored.
     */
    public int getSize() {
        return size;
    }

    /**
     * Adds a new batch at the BEGINNING of the list.
     * 
     * Steps:
     * 1. Make the new node point to the old head
     * 2. Make head point to the new node
     * 3. Increase size
     * 
     * This is O(1) - very fast, does not depend on list size.
     */
    public void addFirst(Batch newNode) {
        newNode.setNext(head);   // new node points to old first node
        head = newNode;          // head now points to the new node
        size++;
    }

    /**
     * Adds a new batch at the END of the list.
     * 
     * Steps:
     * 1. If list is empty -> just make head = newNode
     * 2. Else walk to the last node (the one whose next is null)
     * 3. Make that last node point to the new node
     * 4. Increase size
     * 
     * This is O(n) because we may have to walk the whole list.
     */
    public void addLast(Batch newNode) {
        newNode.setNext(null);   // make sure the new node is the end

        if (head == null) {
            // list was empty
            head = newNode;
        } else {
            // walk until we find the last node
            Batch current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            // now current is the last node
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Searches for a batch by its ID.
     * 
     * Walks from head to the end.
     * Returns the Batch object if found, or null if not found.
     * 
     * This is linear search - O(n)
     */
    public Batch find(String batchId) {
        Batch current = head;

        while (current != null) {
            // equalsIgnoreCase so "b10001" and "B10001" are the same
            if (current.getBatchId().equalsIgnoreCase(batchId)) {
                return current;   // found it!
            }
            current = current.getNext();  // move to next node
        }

        return null;  // not found
    }

    /**
     * Removes a batch by its ID.
     * 
     * Special cases we must handle:
     * - Removing the head (first node)
     * - Removing a middle or last node
     * - Batch not found
     * 
     * Returns true if we successfully removed it, false if not found.
     */
    public boolean remove(String batchId) {
        // Case 1: list is empty
        if (head == null) {
            return false;
        }

        // Case 2: the head is the one we want to remove
        if (head.getBatchId().equalsIgnoreCase(batchId)) {
            head = head.getNext();  // just move head forward
            size--;
            return true;
        }

        // Case 3: the node is somewhere in the middle or end
        Batch previous = head;
        Batch current = head.getNext();

        while (current != null) {
            if (current.getBatchId().equalsIgnoreCase(batchId)) {
                // skip over the current node
                previous.setNext(current.getNext());
                size--;
                return true;
            }
            // move both pointers forward
            previous = current;
            current = current.getNext();
        }

        // we reached the end and never found it
        return false;
    }

    /**
     * Converts the whole linked list into an ArrayList.
     * 
     * Why? Because JTable and other GUI components prefer ArrayList.
     * We walk the linked list once and copy every node into the ArrayList.
     */
    public ArrayList<Batch> getAll() {
        ArrayList<Batch> list = new ArrayList<Batch>();

        Batch current = head;
        while (current != null) {
            list.add(current);
            current = current.getNext();
        }

        return list;
    }

    /**
     * Generates the next available Batch ID.
     * 
     * Example: if the highest ID is B10004, this returns B10005.
     * 
     * How it works:
     * 1. Walk the whole list
     * 2. Look at every ID and find the biggest number
     * 3. Add 1 to that number
     * 4. Make sure the new ID does not already exist (just in case)
     */
    public String nextId() {
        int biggest = 0;
        String prefix = "B";

        Batch current = head;
        while (current != null) {
            String id = current.getBatchId();

            // find where the digits start (from the end)
            int end = id.length();
            int start = end;
            while (start > 0 && Character.isDigit(id.charAt(start - 1))) {
                start--;
            }

            if (start < end) {
                try {
                    int number = Integer.parseInt(id.substring(start, end));
                    if (number > biggest) {
                        biggest = number;
                        prefix = id.substring(0, start);  // keep the letters
                    }
                } catch (NumberFormatException e) {
                    // ignore weird IDs
                }
            }
            current = current.getNext();
        }

        // if list was empty, start at B10001
        if (biggest == 0) {
            return "B10001";
        }

        // try the next number, keep going if that ID already exists
        int next = biggest + 1;
        while (find(prefix + next) != null) {
            next++;
        }

        return prefix + next;
    }

    // =========================================================
    // EXTRA METHODS USED BY THE ORIGINAL UI
    // (kept simple so a student can still understand them)
    // =========================================================

    /**
     * Search the linked list.
     * Returns all batches whose ID, name or category contains the text.
     * If text is empty, returns every batch.
     */
    public ArrayList<Batch> search(String text) {
        ArrayList<Batch> result = new ArrayList<Batch>();
        text = text.trim().toLowerCase();
        Batch current = head;
        while (current != null) {
            if (text.equals("")
                    || current.getBatchId().toLowerCase().contains(text)
                    || current.getName().toLowerCase().contains(text)
                    || current.getCategory().toLowerCase().contains(text)) {
                result.add(current);
            }
            current = current.getNext();
        }
        return result;
    }

    /**
     * Count how many batches have a certain status
     * (Expired / Near expiry / Good).
     * Walks the whole linked list once.
     */
    public int countStatus(String status) {
        int count = 0;
        Batch current = head;
        while (current != null) {
            if (current.getStatus().equals(status)) {
                count++;
            }
            current = current.getNext();
        }
        return count;
    }

    /**
     * Count how many batches belong to a category.
     */
    public int countCategory(String category) {
        int count = 0;
        Batch current = head;
        while (current != null) {
            if (current.getCategory().equalsIgnoreCase(category)) {
                count++;
            }
            current = current.getNext();
        }
        return count;
    }

    /**
     * Returns the batches that expire first (the soonest dates).
     * Simple way: find the earliest, then the next earliest, etc.
     * Not the fastest algorithm, but easy to understand.
     */
    public ArrayList<Batch> getEarliest(int howMany) {
        ArrayList<Batch> result = new ArrayList<Batch>();
        for (int i = 0; i < howMany; i++) {
            Batch best = null;
            Batch current = head;
            while (current != null) {
                if (!result.contains(current)) {
                    if (best == null || current.getExpiration().isBefore(best.getExpiration())) {
                        best = current;
                    }
                }
                current = current.getNext();
            }
            if (best != null) {
                result.add(best);
            }
        }
        return result;
    }

    /**
     * Sorts the linked list using Bubble Sort.
     * We swap the DATA inside the nodes (not the node pointers).
     * This keeps the linked list structure the same.
     *
     * field can be: "Expiration Date", "Batch ID", "Medicine Name",
     *               "Category", "Quantity"
     */
    public void sortBy(String field) {
        if (head == null) {
            return;
        }
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            Batch current = head;
            while (current.getNext() != null) {
                Batch after = current.getNext();
                if (compare(current, after, field) > 0) {
                    swapData(current, after);
                    swapped = true;
                }
                current = current.getNext();
            }
        }
    }

    // helper for sortBy - returns > 0 if a should come after b
    private int compare(Batch a, Batch b, String field) {
        if (field.equals("Expiration Date")) {
            return a.getExpiration().compareTo(b.getExpiration());
        } else if (field.equals("Batch ID")) {
            return a.getBatchId().compareToIgnoreCase(b.getBatchId());
        } else if (field.equals("Medicine Name")) {
            return a.getName().compareToIgnoreCase(b.getName());
        } else if (field.equals("Category")) {
            return a.getCategory().compareToIgnoreCase(b.getCategory());
        } else if (field.equals("Quantity")) {
            return a.getQuantity() - b.getQuantity();
        }
        return 0;
    }

    // helper for sortBy - swaps the data of two nodes
    private void swapData(Batch a, Batch b) {
        String tempId = a.getBatchId();
        String tempName = a.getName();
        int tempQuantity = a.getQuantity();
        java.time.LocalDate tempDate = a.getExpiration();
        String tempCategory = a.getCategory();
        String tempDosage = a.getDosage();
        boolean tempChecked = a.isChecked();

        a.setBatchId(b.getBatchId());
        a.setName(b.getName());
        a.setQuantity(b.getQuantity());
        a.setExpiration(b.getExpiration());
        a.setCategory(b.getCategory());
        a.setDosage(b.getDosage());
        a.setChecked(b.isChecked());

        b.setBatchId(tempId);
        b.setName(tempName);
        b.setQuantity(tempQuantity);
        b.setExpiration(tempDate);
        b.setCategory(tempCategory);
        b.setDosage(tempDosage);
        b.setChecked(tempChecked);
    }
}


package model;//the actuall linkedlist

import java.util.ArrayList;

public class BatchList {
    private Batch head;
    private int size;
    public Batch getHead() {
        return head;
    }
    public int getSize() {
        return size;
    }
    public void addFirst(Batch newNode) {//first node
        newNode.setNext(head);  
        head = newNode;          
        size++;
    }
    public void addLast(Batch newNode) {
        newNode.setNext(null);   // make sure the new node is the end
        if (head == null) {
            //list was empty
            head = newNode;
        } else {
            //walk until we find the last node
            Batch current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            //now current is the last node
            current.setNext(newNode);
        }
        size++;
    }
    public Batch find(String batchId) {//linear search by id
        Batch current = head;
        while (current != null) { 
            if (current.getBatchId().equalsIgnoreCase(batchId)) {
                return current;//found
            }
            current = current.getNext();  //move to next node
        }
        return null;//not found
    }
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
                // skip overhe current node
                previous.setNext(current.getNext());
                size--;
                return true;
            }
            //ove both pointers  forward
            previous = current;
            current = current.getNext();
        }
        //we reached the end and never found it
        return false;
    }
    public ArrayList<Batch> getAll() {
        ArrayList<Batch> list = new ArrayList<Batch>();

        Batch current = head;
        while (current != null) {
            list.add(current);
            current = current.getNext();
        }
        return list;
    }
    public String nextId() {
        int biggest = 0;
        String prefix = "B";

        Batch current = head;
        while (current != null) {
            String id = current.getBatchId();
            // find where the digits start from the end
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
                        prefix = id.substring(0, start);
                    }
                } catch (NumberFormatException e) {
                //ignore
                }
            }
            current = current.getNext();
        }
    
        if (biggest == 0) {//if list is emty it starts with ts
            return "B10001";
        }
        // try the next number, keep going if that ID already exists
        int next = biggest + 1;
        while (find(prefix + next) != null) {
            next++; //reference
        }
        return prefix + next;
    }
//search
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
   //count
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
//how many bitches
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
    //Returns the batches that expire first (the soonest dates).
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
     //Bubble Sort kung mali ilnag order it swap ilang data
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
    }//determines  which of which comes firts
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
        }//positive results means A should come after B
        return 0;
    }//Instead of changing the linked list pointers, it swaps the information stored in nodes
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

        b.setBatchId(tempId); //temporary data ni
        b.setName(tempName);
        b.setQuantity(tempQuantity);
        b.setExpiration(tempDate);
        b.setCategory(tempCategory);
        b.setDosage(tempDosage);
        b.setChecked(tempChecked);
    }
}


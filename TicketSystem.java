package coolio.ticketsystem;
import java.util.Scanner;

/**
 *
 * @author sampa
 * 
 * Main loop to show functionality of ticket list. 
 * Gets input and edits ticket list based off of it. 
 */
public class TicketSystem {

    public static void main(String[] args) {
        // Scanner for input, new ticket list, current answer, loop check, and ticket items
        Scanner scnr = new Scanner(System.in);
        TicketList list = new TicketList();
        int answer;
        boolean loop = true;
        String currID, currDescription, currName;
        
        
        
        while(loop){
            // Options for the user to input
            System.out.print("\n--- Customer Support Ticket System ---\n" +
                "1. Add a Ticket\n" +
                "2. Serve a Ticket\n" +
                "3. Peek Next Ticket\n" +
                "4. Search Ticket by ID\n" +
                "5. Display All Tickets\n" +
                "6. Exit\n" +
                "Enter your choice:");
            
            // Loop until a valid answer is found 
            answer = 0;
            while(answer == 0){
                try{
                    answer = Integer.parseInt(scnr.nextLine());
                    
                    // Ensure its within the correct range
                    if(!(answer >= 1 && answer <= 6)){
                        throw new Exception("Invalid");
                    }
                }
                catch(Exception e){
                    System.out.print("Invalid choice.\nNew choice: ");// Reevaluate choice
                    answer = 0;
                }
            }
            
            switch(answer){
                case 1 -> {
                    // Set details of current ticket
                    System.out.print("Enter Ticket ID:");
                    currID = scnr.nextLine().trim();// Removes leading a trailing whitespace
                    System.out.print("Enter Customer Name:");
                    currName = scnr.nextLine();
                    System.out.print("Enter Issue Descrition:");
                    currDescription = scnr.nextLine();
                    // Adds ticket if ID is free
                    try{
                        list.addTicket(currID, currName, currDescription);
                    }
                    catch(Exception e){
                        System.out.println("ID already exists.");
                    }
                }
                case 2 -> {
                    try{
                        // Serves ticket if pool has ticket to serve
                        System.out.println("Serving Ticket:");
                        list.serve().display();
                    }
                    catch (Exception e){
                        System.out.println("Ticket pool is empty. No ticket to serve.");
                    }
                }
                case 3 -> {
                    try{
                        // Peeks ticket if pool has ticket to peek
                        System.out.println("Next Ticket to Serve:");
                        list.peek().display();
                    }
                    catch (Exception e){
                        System.out.println("Ticket pool is empty. No ticket to serve.");
                    }
                }
                case 4 -> {
                    try{
                        // Searches by ID, All IDs are trimmed here and on creation 
                        System.out.print("Enter ID to search:");
                        currID = scnr.nextLine().trim();
                        list.peekByID(currID).display();
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case 5 -> {
                    try{
                        // Displays all tickets in pool
                        list.displayAll();
                    }
                    catch (Exception e){
                        System.out.println("Ticket pool is empty. No ticket to serve.");
                    }
                }
                // Exits loop
                case 6 -> loop = false;
            }
        }
        
        System.out.println("\n--- Thank You Have a Pleasant Day ---\n");
    }
}

/**
 * 
 * @author sampa
 * 
 * Node that stores ticket information as well as next nodes location.
 */
class Node {
    String id, customer, description;
    Node next;
    
    /**
     * 
     * @param id Unique ticket identification
     * @param customer Customer name
     * @param description Description of issue
     */
    public Node(String id, String customer, String description){
        this.id = id;
        this.customer = customer;
        this.description = description;
    }
    
    /**
     * 
     * @param id Unique ticket identification
     * @param customer Customer name
     * @param description Description of issue
     * @param next Location of next ticket
     */
    public Node(String id, String customer, String description, Node next){
        this.id = id;
        this.customer = customer;
        this.description = description;
        this.next = next;
    }
    
    /**
     * Displays ticket information
     */
    public void display(){
        System.out.println("Ticket ID: " + id + ", Customer Name: " + customer + ", Issue: " + description);
    }
}

/**
 * 
 * @author sampa
 * 
 * Singly linked list with head and tail pointer that utilizes nodes as tickets.
 * Implemented in a queue fashion.
 */
class TicketList {
    Node head, tail;
    
    /**
     * Initialize a list without a head node
     */
    public TicketList(){
        this.head = null;
        this.tail = null;
    }
    
    /**
     * 
     * @param head First node in the list
     * 
     * Initialize a list with a head node
     */
    public TicketList(Node head){
        this.head = head;
        this.tail = head;
    }
    
    /**
     * 
     * @param ticket Reference to a node to be enqueued
     * @throws Exception Checks if ticket ID already exists in list
     * 
     * Enqueues an already existing node to the list, this is done at the tail 
     */
    public void addTicket(Node ticket) throws Exception{
        try{
            this.peekByID(ticket.id); // Checks ID existance
        }catch (Exception e){ // If it doesn't exist peekByID throws exception hence the catch
            if(this.head != null){
                this.tail.next = ticket;
                this.tail = ticket;
            } else {
                this.head = ticket;
                this.tail = ticket;
            }
            System.out.println("Ticket added successfully!");
            return;
        }
        
        // If it doesn't go through catch then the ID exists
        throw new Exception("ID already in use.");
    }

    
    /**
     * 
     * @param id Unique ticket ID
     * @param customer Customer name
     * @param description Descripton of issue
     * @throws Exception Checks if id already exists in list
     * 
     * Enqueues a new node into list based on params, this is done at the tail
     */
    public void addTicket(String id, String customer, String description) throws Exception{
        try{
            this.peekByID(id); // Checks if ID exists
            
        }catch (Exception e){ // If it doesn't exists it throws an exception hence the catch
           Node ticket = new Node(id, customer, description);

            if(this.head != null){
                this.tail.next = ticket;
                this.tail = ticket;
            } else {
                this.head = ticket;
                this.tail = ticket;
            }
            System.out.println("Ticket added successfully!");
            return;
        }
 
        // If you don't go through the catch the ID exists
        throw new Exception("ID already in use.");
    }
    
    /**
     * 
     * @return Output currently at the head of the list
     * @throws Exception Checks if queue is empty
     * 
     * Returns and removes ticket from the head of the list
     */
    public Node serve() throws Exception{
        if(this.head != null){
            Node output = this.head;

            this.head = this.head.next;

            return output;
        } else {
            throw new Exception("Queue empty!!");
        }
    }
    
    /**
     * 
     * @return Output currently add the head of the list
     * @throws Exception Checks if queue is empty
     * 
     * Returns the ticket from the head of the list without removing
     */
    public Node peek() throws Exception{
        if(this.head != null){
            return this.head;
        } else {
            throw new Exception("Queue empty!!");
        }
    }
    
    
    /**
     * 
     * @param id ID of ticket to find
     * @return Ticket with given ID
     * @throws Exception If the list is empty or if the ID doesn't exist
     * 
     * Iterates through the list until a ticket with a matching ID is found.
     * If none is found, or the list is empty throws an exception.
     */
    public Node peekByID(String id) throws Exception{
        if(this.head == null){
            throw new Exception("Ticket pool is empty. No ticket to serve.");
        }
        
        // Check head, then loops through the rest of the list
        Node current = this.head;
        
        while(current != null){
            if(current.id.equals(id)){
                return current;
            }
            current = current.next;
        }
        
        throw new Exception("ID does not exist at this time.");
    }
    
    /**
     * 
     * @throws Exception Checks if queue is empty
     * 
     * Calls each node.display() through the entire list.
     */
    public void displayAll() throws Exception{
        if(this.head == null){
            throw new Exception("Queue empty!!");
        }
        System.out.println("All pending tickets in pool: ");
         
        Node current = this.head;
        
        while(current != null){
            current.display();
            current = current.next;
        }
    }
}
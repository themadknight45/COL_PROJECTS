import java.util.*;
class Customer{
	int id,inTime,outTime,Quantity,Status,CounterOutTime,avlheight,Leftforcooking,receivedBurger,ExcepTime,ExcepStatus;
	Customer NextInQueue,PreviousInQueue;
	Customer left,right;
	Customer(int id,int inTime,int Quantity){
		this.id=id;
		this.inTime = inTime;
		this.Quantity = Quantity;
		this.Leftforcooking=Quantity;
	}
}

class Event{
	int ScheduledTime;
	Runnable Task;
	int DeclarationTime;
	Event next;
	Event(int ScheduledTime,Runnable x,int DeclarationTime){
		this.ScheduledTime= ScheduledTime;
		this.Task =x;
		this.DeclarationTime=DeclarationTime;
	}
}
class PriorityQueue{
	Event front,rear;
	int size=0;
	void add(Event x) {
		if(front == null) {
			front=rear=x;
		}
		else if(front.ScheduledTime>x.ScheduledTime){
			x.next=front;
			front=x;
			
		}
		else {
			Event temp=front;
			while (temp.next!=null&& x.ScheduledTime>=temp.next.ScheduledTime) {
				temp=temp.next;
			}
			if(temp.next==null) {
				rear.next = x;
				rear = x;
			}
			else {
				x.next=temp.next;
				temp.next=x;
			}
		}
		size++;
	}
	Event peek() {
		return front;
	}
	Event poll() {
		Event temp= front;
		front=front.next;
		if(front==null) {
			rear = null;
		}
		size--;
		return temp;
	}
	boolean isEmpty() {
        return size==0;
    }
}
class Queue1{
	Customer front,rear;
	int size,id;
	void enqueue(Customer x){
		if(front==null) {
			front = rear =x;
		}
		else {
			rear.NextInQueue = x;
			x.PreviousInQueue=rear;
			rear = x;
		}
		size++;
	}
	Customer dequeue(){
		if (front == null) {
			return null;
		}
		Customer temp = front;
		front = front .NextInQueue;
		if (front == null) {
			rear = null;
		}
		else {
			front.PreviousInQueue=null;
		}
		size--;
		return temp;
	}
	boolean isEmpty() {
        return size==0;
    }
}



class Counter{
	int size;
	int id;
	int firstCustomerDepartTime;
	Counter(int id){
		this.id = id;
		firstCustomerDepartTime=-1;
	}
}


public class MMBurgers implements MMBurgersInterface {
	int LastCmdTime=-1;
	public static int K;          //no. of counters
	public static int N=0;  		//no. of active customers
	public static int Total=0;    //Total customers visiting restaurant
	public static int time =0;
	public static int TotalWaitingTime=0;
	public static Customer avlroot;
	static Counter[]counters;
	public static int BurgerOnPan,BurgerinWaiting,TotalOrder,MaxPanCapacity =0;
	public static Queue1 ChefQueue = new Queue1();
	public static PriorityQueue EventQueue =new PriorityQueue();
	
	private  class CounterDeparture extends MMBurgers implements Runnable{
		  public int ScheduledTime;
		  int id;
		  CounterDeparture (int id,int ScheduledTime){
			  this.id = id;
			  this.ScheduledTime= ScheduledTime;
		  }
		public  void run() {
//			System.out.println(id+" is departing from Counter");
			if(time==ScheduledTime) {
			Customer x = search(id);
			x.CounterOutTime = time;
//			h.remove(counters[x.Status-1]);
			counters[x.Status-1].size--;
			if(counters[x.Status-1].size==0) {
				counters[x.Status-1].firstCustomerDepartTime=-1;
			}
			else {
				counters[x.Status-1].firstCustomerDepartTime +=x.Status;
			}
//			h.insert(counters[x.Status-1]);
			ChefQueue.enqueue(x);
				x.Status = K+1;
				int p =MaxPanCapacity-BurgerOnPan;
				if(BurgerOnPan<MaxPanCapacity) {
					if(BurgerOnPan+x.Quantity<= MaxPanCapacity) {
						BurgerOnPan+= x.Quantity;
						x.Leftforcooking=0;
						PattyRemoval e = new PattyRemoval(time+10,x.Quantity);
						Event y = new Event(time+10,e,time);
						EventQueue.add(y);
					}
					else {
						BurgerinWaiting+= x.Quantity-BurgerOnPan;
						x.Leftforcooking=x.Quantity-p;
						BurgerOnPan = MaxPanCapacity;
						PattyRemoval e = new PattyRemoval(time+10,p);
						Event y = new Event(time+10,e,time);
						EventQueue.add(y);
					}
				}
				else {
					BurgerinWaiting+= x.Quantity;
				}
				TotalOrder+=x.Quantity;
				EventQueue.poll();
			}
		}
	}
	private class PattyRemoval extends MMBurgers implements Runnable{
		public int ScheduledTime;
		public int Quantity;
		PattyRemoval(int ScheduledTime,int Quantity){
			this.ScheduledTime = ScheduledTime;
			this.Quantity = Quantity;
		}
		public void run() {
			if(time==ScheduledTime) {
//				System.out.println(ChefQueue.size+"Chef q size");
				ChefQueue.front.receivedBurger+=Quantity;
				TotalOrder-=Quantity;
				BurgerOnPan-=Quantity;
//				System.out.println("Removed "+Quantity+"burgers for customer "+ChefQueue.front.id);
				if(ChefQueue.front.receivedBurger==ChefQueue.front.Quantity) {
					Customer x = ChefQueue.dequeue();
					Customer y= search(x.id);
//					System.out.println(y.id+" will leave the restaurant "+"at t ="+time);
					y.outTime = time+1;
					TotalWaitingTime += y.outTime-y.inTime;
					y.ExcepTime=time;
					y.ExcepStatus=y.Status;
					y.Status+=1;
					N--;
				}
				Customer temp =ChefQueue.front;
				while(Quantity>0&&temp!=null) {
					if(Quantity-temp.Leftforcooking>=0 ) {
						PattyRemoval e = new PattyRemoval(time+10,temp.Leftforcooking);
						BurgerOnPan+=temp.Leftforcooking;
						BurgerinWaiting-=temp.Leftforcooking;
//					System.out.println("Scheduled a new Event of patty removal of "+Quantity+ "burgers at time"+ time+"for"+temp.id);
						Event y = new Event(time+10,e,time);
						EventQueue.add(y);
						Quantity-=temp.Leftforcooking;
						temp.Leftforcooking=0;
						temp=temp.NextInQueue;
					}
					else if(Quantity-temp.Quantity<0) {
						PattyRemoval e =new PattyRemoval(time+10,Quantity);
						Event y = new Event(time+10,e,time);
						EventQueue.add(y);
						temp.Leftforcooking-=Quantity;
						BurgerOnPan+=Quantity;
						BurgerinWaiting-=Quantity;
//						System.out.println("Added "+Quantity+"burgers for customer "+temp.id);
						Quantity=0;
					}
				}
				EventQueue.poll();
			}
		}
	}
	
	int height(Customer j) {
        if (j == null)
            return 0;
 
        return j.avlheight;
    }
    
    public Customer search(int id) {
    	Customer temp = avlroot;
    	while(temp!=null &&temp.id!=id) {
    		if(temp.id<id) {
    			temp = temp.right;
    		}
    		if(temp.id>id) {
    			temp = temp.left;
    		}
    	}
    	return temp;
    }
    int max(int a, int b) {
        return (a > b) ? a : b;
    }
    
    Customer rightRotate(Customer y) {
    	Customer x = y.left;
    	Customer T2 = x.right;
 
        // Perform rotation
        x.right = y;
        y.left = T2;
 
        // Update heights
        y.avlheight = max(height(y.left), height(y.right)) + 1;
        x.avlheight = max(height(x.left), height(x.right)) + 1;
 
        // Return new root
        return x;
    }
 
    Customer leftRotate(Customer x) {
    	Customer y = x.right;
    	Customer T2 = y.left;
 
        // Perform rotation
        y.left = x;
        x.right = T2;
 
        //  Update heights
        x.avlheight = max(height(x.left), height(x.right)) + 1;
        y.avlheight = max(height(y.left), height(y.right)) + 1;
 
        // Return new root
        return y;
    }
 
    // Get Balance factor of node N
    int getBalance(Customer N) {
        if (N == null)
            return 0;
 
        return height(N.left) - height(N.right);
    }
 
    Customer insert(Customer node, Customer x) {
 
        /* 1.  Perform the normal BST insertion */
        if (node == null)
            return x;
 
        if (x.id < node.id)
            node.left = insert(node.left, x);
        else if (x.id > node.id)
            node.right = insert(node.right, x);
        else // Duplicate keys not allowed
            return node;
        
        node.avlheight = 1 + max(height(node.left),
                              height(node.right));
 
        int balance = getBalance(node);
 
      
        if (balance > 1 && x.id < node.left.id)
            return rightRotate(node);
 
        if (balance < -1 && x.id > node.right.id)
            return leftRotate(node);
        if (balance > 1 && x.id > node.left.id) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && x.id < node.right.id) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }
	
	
    public boolean isEmpty(){
        return N==0;
    } 
    
    public void setK(int k) throws IllegalNumberException{
    	if(k<=0) {
    		throw new IllegalNumberException("no of queue cant be negative");
    	}
       	K =k;
       	counters = new Counter[k];
       	for(int i =0;i<k;i++) {
       		counters[i]=new Counter(i+1);
       	}
//       	h = new Heap(k);
//       	for(int i =0;i<k;i++) {
//       		h.insert(counters[i]);
//       	}
//       	h.print();
    }   
    
    public void setM(int m) throws IllegalNumberException{
    	if(m<=0) {
    		throw new IllegalNumberException("burger capacity cant be negative");
    	}
        MaxPanCapacity=m;	
//        System.out.println(m);
    } 

    public void advanceTime(int t) throws IllegalNumberException{
//    	System.out.println("Ab mai karunga advance time.present time is "+time);
	        while(time<=t && EventQueue.size!=0 && N!=0) {
	        	Runnable x =EventQueue.peek().Task;
	        	int k = EventQueue.peek().ScheduledTime;
//	        	System.out.println("Next Scheduled Time is "+k +" & Size of task queue is "+EventQueue.size);
	        	if (k<=t){
	        		time=k;
	        		x.run();
	        	}
	        	else if(k>t){ 
	        		time=t;
	        		break;
	        	}
	        }
    	LastCmdTime=t;
        time=t;
    } 

    public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException{
    	if (t< time) {
    		throw new IllegalNumberException("invalid time in arrive customer");
    	}
    	if(t>LastCmdTime) {
    	advanceTime(t);
    	}
    	Customer c = new Customer(id,t,numb);
    	Counter temp= counters[0];
    	for(int i=0;i<counters.length;i++) {
    		if (temp.size>counters[i].size) {
    			temp=counters[i];
    		}
    	}
    	
//    	Counter q= h.removeMin();
//    	if(q.size==0) {
//    		q.firstCustomerDepartTime=t+q.id;
//    	}
    	if(temp.size==0) {
    		temp.firstCustomerDepartTime=t+temp.id;
    	}
//    	h.print();
    	c.Status=temp.id;
//    	System.out.println(c.Status);
    	counters[temp.id-1].size =temp.size+1;
//    	h.insert(counters[q.id-1]);
//    	h.print();
    	avlroot= insert(avlroot,c);
    	N++;
    	Total++;
    	CounterDeparture e = new CounterDeparture(id,temp.firstCustomerDepartTime+((temp.size-1)*temp.id));
    	Event y = new Event(temp.firstCustomerDepartTime+((temp.size-1)*temp.id),e,t);
		EventQueue.add(y);
//    	System.out.println("Size of taskq is "+TaskQueue.size());
    	LastCmdTime=t;
    } 

    public int customerState(int id, int t) throws IllegalNumberException{
    	if (t< time) {
    		throw new IllegalNumberException("invalid time in Customer State");
    	}
    	if(t>LastCmdTime) {
//    		System.out.println(" aa gye idhar customer state me");
//    		System.out.println("Size of taskq is "+TaskQueue.size());
    		advanceTime(t);
    		}
        Customer x= search(id);
//        System.out.println(x.id);
        if (x==null) {
        	throw new IllegalNumberException("invalid ID being searched in customer State");
        }
        if(t==x.ExcepTime) {
        	return x.ExcepStatus;
        }
        LastCmdTime=t;
        return x.Status; 
    } 

    public int griddleState(int t) throws IllegalNumberException{
    	if(t>LastCmdTime) {
//    		System.out.println("I'm into finding griddle state at time "+t);
    		advanceTime(t);
    		}
    	if (t< time) {
    		throw new IllegalNumberException("invalid time in Griddle State");
    	}
    	LastCmdTime=t;
    	return BurgerOnPan;
    } 

    public int griddleWait(int t) throws IllegalNumberException{
    	if(t>LastCmdTime) {
    		advanceTime(t);
    		}
    	if (t< time) {
    		throw new IllegalNumberException("invalid time in Griddle wait");
    	}
    	LastCmdTime=t;
    	return TotalOrder-BurgerOnPan;
    } 

    public int customerWaitTime(int id) throws IllegalNumberException{
        if(!isEmpty()) {
        	throw new IllegalNumberException("counters are not all empty in customer wait time");
        }
        Customer x= search(id);
        if (x==null) {
        	throw new IllegalNumberException("invalid ID being searched in customer State");
        }
        
        return x.outTime; 
    } 
                                                                            
	public float avgWaitTime(){
		float x = TotalWaitingTime/Total;
		K=0;
		N=0;
		time=0;
		Total=0;
		TotalWaitingTime=0;
		avlroot=null;
		counters=null;
		BurgerOnPan=0;
		BurgerinWaiting=0;
		TotalOrder=0;
		MaxPanCapacity=0;
		ChefQueue=null;
		return x;
    } 
	
	
}

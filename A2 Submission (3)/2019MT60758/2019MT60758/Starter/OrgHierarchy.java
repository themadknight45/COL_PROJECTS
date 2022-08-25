import java.io.*; 
import java.util.*;
class Queue{
	Node front,rear;
	int size;
	public Queue() {
		this.rear =null;
		this.front = null;
		size =0;
	}
	void enqueue(Node node) {
		if(front==null) {
			front = rear = node;
		}
		else {
			rear.next = node;
			rear = node;
		}
		size++;
	}
	Node dequeue(){
		if (front == null) {
			return null;
		}
		Node temp = front;
		front = front .next;
		if (front == null) {
			rear = null;
		}
		size--;
		return temp;
	}
	boolean isEmpty() {
        return size==0;
    }
	String toStr() {
		String s ="";
		if(isEmpty()) {
			return null;
		}
		Node temp = front;
		while(temp!=null) {
			s=s+temp.Id+",";
			temp=temp.next;
		}
		return s;
	}
}
// Tree node
class Node {
  int Id;
  int level;
  int height;
  boolean Inavltree;
  Vector<Node>child;
  Node left,right,parent,next;
  Node(int id){
	  this.Id = id;
	  this.level = 1;
	  this.height =1;
	  this.child = new Vector<>();
	  Inavltree=false;
  }
}


public class OrgHierarchy implements OrgHierarchyInterface{
//root node
Node root;
Node avlroot;
int size;
private int height(Node temp) {
	if (temp== null) {
		return 0;
	}
	return temp.height;
}
private int max(int x, int y) {
	if(x>y) {
		return x;
	}
	return y;
}
private Node rightRotate(Node b) {
    Node a = b.left;
    Node c = a.right;

    a.right = b;
    b.left = c;

    b.height = max(height(b.left), height(b.right)) + 1;
    a.height = max(height(a.left), height(a.right)) + 1;

    return a;
}

private Node leftRotate(Node a) {
    Node b = a.right;
    Node c = b.left;

    b.left = a;
    a.right = c;

    a.height = max(height(a.left), height(a.right)) + 1;
    b.height = max(height(b.left), height(b.right)) + 1;

    return b;
}

private int balance_factor(Node temp) {
    if (temp == null) {
        return 0;
    }
    return height(temp.left) - height(temp.right);
}


public Node insert(Node node, Node temp,Node boss) {
    if (node == null) {
    	boss.child.add(temp);
    	temp.parent = boss;
    	temp.level= boss.level+1;
    	size = size +1;
    	temp.Inavltree=true;
    	return temp;
    }
    if (temp.Id < node.Id) {
        node.left = insert(node.left, temp,boss);
    }
    else if (temp.Id > node.Id) {
        node.right = insert(node.right, temp,boss);
    }
    else {
    	boss.child.add(temp);
    	temp.parent = boss;
    	temp.level= boss.level+1;
    	size = size +1;
    	temp.Inavltree=true;
    	return temp;
    }
    
    node.height = 1 + max(height(node.left),height(node.right));

    int balance = balance_factor(node);

    if (balance > 1 && temp.Id < node.left.Id)
        return rightRotate(node);

    // Right Right Case
    if (balance < -1 && temp.Id > node.right.Id)
        return leftRotate(node);

    // Left Right Case
    if (balance > 1 && temp.Id > node.left.Id) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }

    // Right Left Case
    if (balance < -1 && temp.Id < node.right.Id) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }

    /* return the (unchanged) node pointer */
    return node;
}

private Node search(int id,Node temp) {
	if(temp==null||(temp.Id==id )) {
		return temp;
	}
	else if( temp.Id>id ) {
		return search(id,temp.left);
	}
	else{
		return search(id,temp.right);
	}
}

public boolean isEmpty(){
	return root == null;
} 

public int size(){
	return size;
}

public int level(int id) throws IllegalIDException, EmptyTreeException{
	if (isEmpty()) {
		throw new EmptyTreeException("Empty tree");
	}
	Node temp = search(id,avlroot);
	if(temp == null) {
		throw new IllegalIDException("ID not found");
	}
	return temp.level;	
} 

public void hireOwner(int id) throws NotEmptyException{
	if(! isEmpty()) {
		throw new NotEmptyException("tree not empty");
	}
	root = new Node(id);
	avlroot = root;
	size =1;
	root.level =1;
	root.Inavltree = true;
}

public void hireEmployee(int id, int bossid) throws IllegalIDException, EmptyTreeException{
	if (isEmpty()) {
		throw new EmptyTreeException("Empty tree");
	}
	Node temp = new Node(id);
	Node boss = search(bossid,avlroot);
	if(boss == null) {
		throw new IllegalIDException("ID not found");
	}
	this.avlroot= this.insert(avlroot,temp,boss);
} 

public void fireEmployee(int id) throws IllegalIDException,EmptyTreeException{
	if (isEmpty()) {
		throw new EmptyTreeException("Empty tree");
	}
	Node temp =search(id,avlroot);
	if(temp == null ) {
		throw new IllegalIDException("ID not found");
	}
	if(temp == root) {
		throw new IllegalIDException("trying to delete root");
	}
	if(!temp.child.isEmpty()) {
		throw new IllegalIDException("temp child is not null");
	}
	Node x = temp.parent;
	int i = x.child.indexOf(temp);
	x.child.remove(i);
	temp.Inavltree= false;
	size = size-1;
}

public void fireEmployee(int id, int manageid) throws IllegalIDException,EmptyTreeException{
	if (isEmpty()) {
		throw new EmptyTreeException("Empty tree");
	}
	Node temp1 =search(id,avlroot);
	Node temp2 = search(manageid,avlroot);
	if(temp1==null||temp2==null||temp1.level!=temp2.level) {
		throw new IllegalIDException("ID not found");
	}
	Node a,d;
	Vector<Node> c,f;
	a = temp1.parent;
	c = temp1.child;
	d = temp2.parent;
	f = temp2.child;
	if(a==d) {
		for (int p = 0; p < temp1.child.size(); p++) {
			temp1.child.get(p).parent=temp2;
			temp2.child.add(temp1.child.get(p));
		}
		int i = a.child.indexOf(temp1);
		a.child.remove(i);
		temp1.Inavltree=false;
	}
	else {
	a.child.add(temp2);
	temp2.parent =a;
	int i = a.child.indexOf(temp1);
	a.child.remove(i);
	int j = d.child.indexOf(temp2);
	d.child.remove(j);
	for (int p = 0; p < temp1.child.size(); p++) {
		temp1.child.get(p).parent=temp2;
		temp2.child.add(temp1.child.get(p));
	}
	temp1.Inavltree = false;
	}
		size = size-1;
} 

public int boss(int id) throws IllegalIDException,EmptyTreeException{
	if (isEmpty()) {
		throw new EmptyTreeException("Empty tree");
	}
	Node temp =search(id,avlroot);
	if(temp==null) {
		throw new IllegalIDException("ID not found");
	}
	if(id==root.Id) {
		return -1;
	}
	return temp.parent.Id ;
}

public int lowestCommonBoss(int id1, int id2) throws IllegalIDException,EmptyTreeException{
	if (isEmpty()) {
		throw new EmptyTreeException("Empty tree");
	}
	Node temp1= search(id1,avlroot);
	Node temp2= search(id2,avlroot);
	if(temp1==null || temp2==null) {
		throw new IllegalIDException("ID not found");
	}
    int x = temp1.level;
    int y = temp2.level;
    Node n;
    if(x<y) {
    	while (temp2.level>temp1.level) {
    		temp2 =temp2.parent;
    	}
    	while(temp1!=temp2) {
    		temp1 = temp1.parent;
    		temp2 = temp2.parent;
    	}
    	return temp1.Id;
    }
    else if(x>y) {
    	while(temp1.level>temp2.level) {
    		temp1 = temp1.parent;
    	}
    	while(temp1!=temp2) {
    		temp1 = temp1.parent;
    		temp2 = temp2.parent;
    	}
    	return temp1.Id;
    }
    else {
    	while(temp1!=temp2) {
    		temp1 = temp1.parent;
    		temp2 = temp2.parent;
    	}
    	return temp1.Id;
    }
	
}


	public String toString(int id) throws IllegalIDException, EmptyTreeException{
		if (isEmpty()) {
			throw new EmptyTreeException("Empty tree");
		}
		Node temp = search(id,avlroot);
		
		if(temp ==null) {
			throw new IllegalIDException("ID not found");
		}
		Queue q = new Queue();
		String s ="";
		q.enqueue(temp);
		while(!q.isEmpty()) {
			int levelsize = q.size;
			Vector <Integer> v = new Vector<>();
			while (levelsize>0) {
				Node n = q.dequeue();
				n.next=null;
				v.add(n.Id);
				for (int i = 0; i < n.child.size(); i++) {
	                q.enqueue(n.child.get(i));

				}
	            levelsize--;
			}
			Collections.sort(v);
			for(int j=0;j<v.size();j++) {
				s = s+v.get(j)+" ";
			}
			if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' ') {
		        s = s.substring(0, s.length() - 1);
		    }
			s= s+",";
		}
		if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == ',') {
	        s = s.substring(0, s.length() - 1);
	    }
	    return s;
	}


}

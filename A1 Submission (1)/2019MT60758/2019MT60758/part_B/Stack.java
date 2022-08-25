// You should utilise your implementation of ArrayDeque methods to implement this
public class Stack implements StackInterface {
	ArrayDeque stack;
	int  top;
	Stack(){
		stack = new ArrayDeque();
		top = -1;
	}
	public void push(Object o){
		top++;
		stack.insertLast(o);
  }

	public Object pop() throws EmptyStackException{
		top--;
		try{
		return stack.removeLast();
	  }
		catch(Exception e){
			System.out.println("Could not do stuff :)");
		}
		return null;
	}

	public Object top() throws EmptyStackException{
		try{
		return stack.last();
	  }
		catch(Exception e){
			System.out.println("Could not do stuff :)");
		}
		return null;
	}

	public boolean isEmpty(){
		return top == -1;
	}

    public int size(){
			return top+1;
    }
}

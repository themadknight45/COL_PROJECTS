public class ArrayDeque implements DequeInterface {
  Object[] deque;
  int front;
  int rear;
  int size;
  ArrayDeque(){
    deque = new Object[1];
    front = -1;
    rear = -1;
    size = 1;
  }

  private boolean isfull(){
    if(front == -1){
      return false;
    }
    else if(size==1 && front==0 && rear==0){
      return true;
    }
    return(size+front-rear)%size == 1 ;
  }

  private Object[] doubleSize(){
    Object[]x = new Object[2*size];
    for (int i =0; i<size;i++){
      x[i] = deque[(i+front)%size];
    }
    return x;
  }

  public void insertFirst(Object o){
    if (isfull()){
      deque = doubleSize();
      front =0;
      rear = size-1;
      size = deque.length;
      insertFirst(o);
    }
    else if (front == -1) {
      front =0;
      rear = 0;
      deque[0]=o;
      //System.out.println(o.toString());
      return ;
    }
    else{
      front = (size+front-1) % size;
      deque[front] = o ;
      //System.out.println(o.toString());
    }
  }

  public void insertLast(Object o){
    if(isfull()){
      deque =doubleSize();
      front =0;
      rear = size -1 ;
      size = deque.length;
      insertLast(o);
    }
    else if(front == -1){
      front =0;
      rear = 0;
      deque[0]=o;
    }
    else{
      rear =  (size+rear+1)% size ;
      deque [rear] =o;
    }
  }

  public Object removeFirst() throws EmptyDequeException{
    if(front == rear ){
      Object m  = deque[front];
      front =-1; rear = -1;
      return (m);
    }
    else {
      Object k = deque[front];
      front = (size+front+1)% size ;
      return k;
    }
  }

  public Object removeLast() throws EmptyDequeException{
    if(front == rear ){
      Object m = deque[front];
      front =-1; rear = -1;
      return m;
    }
    else{
      Object k = deque[rear];
      rear = (size+rear-1)% size ;
      return k;
    }
  }

  public Object first() throws EmptyDequeException{
    return deque[front];
  }

  public Object last() throws EmptyDequeException{
    return deque[rear];
  }

  public int size(){
    if (front == -1){
      return 0;
    }
    if (rear>= front){
      return rear-front+1;
    }
    return size+rear-front+1;
  }

  public boolean isEmpty(){
    if(front == -1){
      return true;
    }
    return false;
  }

  public String toString(){
    int i = front;
    if(front == -1){
      return "[]";
    }
    String s = "";
    while (i!=rear){
      String t = deque[i].toString();
      s = s + t +","+" ";
      i = (size+i+1)%size;
    }
    s = s + deque[rear].toString();
    s = "[" + s+ "]";
    return s;
  }
}

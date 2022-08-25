import java.io.File;
import java.util.*;
public class FabricBreakup {
	public static void main(String args[]){
		Stack primaryStack = new Stack();
		Stack indexStack = new Stack();
		Stack maxStack = new Stack();
		File file = new File(args[0]);
		try{
			Scanner sc = new Scanner(file);
      			int n      = sc.nextInt();
			for (int i =0; i<n;i++){
				int Serial = sc.nextInt();
				int Id = sc.nextInt();
				if( Id == 1){
					int Score = sc.nextInt();
					if(primaryStack.isEmpty()){
						primaryStack.push(Score);
						maxStack.push(Score);
						indexStack.push(1);
					}
					else if((int)maxStack.top()<=Score){
						primaryStack.push(Score);
						maxStack.push(Score);
						indexStack.push(primaryStack.size());
					}
					else{
						primaryStack.push(Score);
					}
				}
				else if(Id ==2){
					if(primaryStack.isEmpty()){
						System.out.println(Serial+ " -1");
					}
					else{
						System.out.println(Serial+" "+ ((int)primaryStack.size()- (int)indexStack.top()));
						primaryStack.top = (int)indexStack.top()-1;
						primaryStack.stack.rear = primaryStack.top;
						indexStack.pop();
						maxStack.pop();
						primaryStack.pop();
						
					}
				}
			}
		}
		catch(Exception e){
			System.out.println("error somewhere");
		}
	}
}

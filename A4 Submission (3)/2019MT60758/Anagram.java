
	import java.io.*;
	import java.io.InputStream;
	import java.util.*;
	import java.util.Arrays;
	import java.util.ArrayList;
	import java.util.HashSet;
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.util.StringTokenizer;
	import java.io.FileInputStream; 

	class FastReader {
	    BufferedReader buff_R;
	    StringTokenizer s_token;

	    public FastReader(InputStream f)
	    {
	        buff_R = new BufferedReader(
	            new InputStreamReader(f));
	    }

	    int nextInt() { 
	    	return Integer.parseInt(next()); 
	    	}
	    
	    String next()
	    {
	        while (s_token == null || !s_token.hasMoreElements()) {
	            try {
	                s_token = new StringTokenizer(buff_R.readLine());
	            }
	            catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return s_token.nextToken();
	    }
	    
	    String nextLine()
	    {
	        String str = "";
	        try {
	            str = buff_R.readLine();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        return str;
	    }
	}
	
	class hashTable{
		private int table_Size;
		static int collisions=0;
		private int occupied=0;
		private ArrayList<Node>[] h_Table;
		
		private class Node {
	        private String key;
	        private ArrayList<String> val_pair = new ArrayList<>();
	        public Node(String key, String value_String) {
	            this.key = key;
	            val_pair.add(value_String);
	        }
	    }
		
		public ArrayList<String> get(String str) {
	        String Key_String =  Character_sort(str);
	        int hash =  hash(Key_String);
	        if (  h_Table[hash] == null) {
	        	return new ArrayList<String>(); 
	        	}

	        for (Node x:  h_Table[hash]) {
	            if (x.key.equals(Key_String) ) { 
	            	return x.val_pair; 
	            }
	        }
	        return new ArrayList<String>();
	    }
		
	   int hash(String str) {
            int val = 0;
            for (int i = 0; i < str.length(); i++) {
                val = (val << 5) | (val >>> 27); 
                val += (int) str.charAt(i); 
            }
            return  Math.floorMod(val,table_Size);
        }
		
		@SuppressWarnings("unchecked")
	    public hashTable(String file_name) throws Exception {
			InputStream inputStream = new FileInputStream(file_name);
	        FastReader sc = new FastReader(inputStream);
	        int vocabularySize = sc.nextInt();
	         table_Size = vocabularySize;
	        h_Table = new ArrayList[ table_Size];
	        long time1 = System.currentTimeMillis();
	        for (int i=0; i<vocabularySize; i++) {
	            String word = sc.nextLine();
	             insert(word);
	        }
	    }
		
		private static String Character_sort(String original_String) {
	        char[] characters = original_String.toCharArray();
	        Arrays.sort(characters);
	        return new String(characters);
	    }
		
		public void insert(String s) {
	        String Key_String = Character_sort(s);
	        int hash =  hash(Key_String);
	        if ( h_Table[hash] == null) { 
	        	h_Table[hash] = new ArrayList<>();
	        }
	        for (Node b:  h_Table[hash]) {
	            if (b.key.equals(Key_String )) {
	                    b.val_pair.add(s);
	                    return;
	            }
	        }
	          h_Table[hash].add(new Node(Key_String, s));
	    }
		
	}


	public class Anagram {
	    public hashTable myTable;
	    
	    public Anagram(String file_Name) throws Exception {
	        myTable = new hashTable(file_Name);
	    }
	    
	    private static String sort(String main) {
	        char[] character_Array = main.toCharArray();
	        Arrays.sort(character_Array);
	        return new String(character_Array);
	    }

	    private ArrayList<String> anagramArray(String s) {
	        ArrayList<String> temp = new ArrayList<>();
	        temp.addAll(cut(s, 1));
	        if(s.length()>=2) {
	        	temp.addAll(cut(s, 2));
	        }
	        if(s.length()>=3) {
	        	temp.addAll(cut(s, 3));
	        }
	        return temp;
	    }

	    private ArrayList<String> cut(String str,int i) {
	    	if(i==1) {
	    		return myTable.get(str);
	    	}
	    	else if(i>=2) {
	    		Set<String> Combinations = new HashSet<>();
	    		ArrayList<String> Combo = new ArrayList<>();
	    		combine(str,0,Combinations,new StringBuffer());
	    		Combinations.remove(sort(str));
	    		for(String s:Combinations) {
	    			ArrayList<String>First= myTable.get(s);
	    			if (!First.isEmpty()) {
		    			String complement = Complement(str,s);
		    			if( i==2 ) {
		    				ArrayList<String>Second= myTable.get(complement);
		    				if(!Second.isEmpty()) {
			    				for(String u:First) {
			    					for(String v:Second) {
			    						String w = u+" "+v;
			    							Combo.add(w);
			    					}
			    				}
		    				}
		    			}
		    			else{
		    			     ArrayList<String>secondComplement= cut(complement,2);
		    			     for(String u:First) {
		    			    	 for(String v:secondComplement) {
		    			    		 String w =u+" "+v;
		    			    		 Combo.add(w);
		    			    	 }
		    			     }
		    			}
		    		}
	    		}
	    		return Combo;
	    	}
	    	return null;
	    }
	    
	    
	    void combine(String input_string,int ind ,Set<String>Combinations,StringBuffer output_string)
	    {
	        for (int i = ind; i < input_string.length(); i++)
	        {	
	            output_string.append(input_string.charAt(i));
	            if(! myTable.get(output_string.toString()).isEmpty() ){
		            Combinations.add(sort(output_string.toString()));
	            }
		            combine(input_string,i+1,Combinations, output_string);
		            output_string.deleteCharAt(output_string.length() - 1);   
	        }
	    } 
	    
	    private String Complement(String main, String unwanted) {
	        StringBuilder x = new StringBuilder(main);
	        for (int i=0; i< unwanted.length(); i++) {
	            int position = x.indexOf(Character.toString(unwanted.charAt(i)));
	            	x.deleteCharAt(position); 
	        }
	        return x.toString();
	    } 
	    
	    
	    public static  void main(String[] args) throws Exception {
	    	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
	        String v  = args[0];
	        String inp = args[1];
	        InputStream inputStream = new FileInputStream(inp);
	        FastReader sc = new FastReader(inputStream);
	        int inputLength = sc.nextInt();
	        Anagram ana = new Anagram(v);
	        for (int i=0; i<inputLength; i++) {
	        	String x =sc.nextLine();
	            ArrayList<String> listOfAnagrams = ana.anagramArray(x);
	            Collections.sort(listOfAnagrams);
	            for (String term: listOfAnagrams) { out.write(term + "\n"); }
	            if(i==inputLength-1) {
	            	out.write("-1");
	            	break;
	            }
	            out.write("-1" + "\n");
	        }
	        out.flush();
	    }
	}


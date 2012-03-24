
/* it assumes that the frequent patterns are stored in a text file in a serialized form*/
package fpgrowth;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;



class FPGrowth implements Serializable {
	
	HashMap<String,Long>fpg_patterns_stored=new HashMap<String,Long>();
	HashMap<String,Long>rules=new HashMap<String,Long>();

	
	public void initializeKeyMap(HashMap<String,Long> patterns){
		for(Object key: patterns.keySet()){
			
			String []elements=key.toString().split(",");
			Arrays.sort(elements);
			int counter=0;
			String temp_string="";
			for(String element:elements){
				if(counter==0){
					temp_string=element;
					counter++;
				}
				temp_string+=","+element;
			}
			this.fpg_patterns_stored.put(temp_string,patterns.get(key) );
			
		}
		
		
		
		
	}
	public void add_rule(ArrayList stack1, ArrayList stack2){
		Collections.sort(stack1);
		Collections.sort(stack2);
		
		String left_side="",right_side="";
		int counter=0;
		for(Object temp: stack1){
			if(counter==0){
				left_side=temp.toString();
				counter++;
			}
			left_side=left_side+","+temp.toString();
		}
		counter=0;
		for(Object temp: stack2){
			if(counter==0){
				right_side=temp.toString();
				counter++;
			}
			right_side=right_side+","+temp.toString();
		}
		
		this.rules.put(left_side+"-->"+right_side, this.fpg_patterns_stored.get(left_side+","+right_side)/this.fpg_patterns_stored.get(left_side));
		
		
	}
	
	public void extract_rules(String[] elements, ArrayList stack1,ArrayList stack2,int index){
		
		if(elements.length<=index&&stack1.size()!=0||stack2.size()!=0){
			add_rule(stack1,stack2);
			return;
			
		}
		if(elements.length<=index){
			return;
		}
		
		for(int i=index;i<elements.length;i++){
			String element=elements[i];
			stack1.add(element);
			extract_rules(elements,stack1,stack2,i+1);
			stack1.remove(stack1.size()-1);
			stack2.add(element);
			extract_rules(elements,stack1,stack2,i+1);
			stack2.remove(stack2.size()-1);
			extract_rules(elements,stack1,stack2,i+1);
			
		}
		
		
		
	}
	
	public void getRules(){
		
		for(String pattern:this.fpg_patterns_stored.keySet()){
			
			String elements[]= pattern.toString().split(",");
			ArrayList stack1= new ArrayList(),stack2=new ArrayList();
			extract_rules(elements,stack1,stack2,0);
			
				
			}	
		
		
	}
	
	public static void main(String args[])  throws IOException, ClassNotFoundException{
		
		ObjectInputStream  reader= new ObjectInputStream(new FileInputStream("read.txt"));
		Object line;
		HashMap<String,Long> patterns=new HashMap<String,Long>();
		while((line=reader.readObject())!=null){
			
			patterns=(HashMap<String,Long>)line;
		}
		FPGrowth fpg= new FPGrowth();
		for(Object key:patterns.keySet()){
			fpg.initializeKeyMap(patterns);
			
		}
		
		
	}
	
	
	
	
}
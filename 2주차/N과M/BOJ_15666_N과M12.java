package N과M;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class BOJ_15666_N과M12 {
	static int n,m;
	static int[] arr;
	static int[] arr2;
	static PrintWriter pw=new PrintWriter(System.out);
	
	static int combination(int idx,int pre) {
		int ret=-1;
		if(idx==m) {
			for(int a:arr2) {
				ret=a;
				pw.print(a+" ");
			}
			pw.println();
			return ret;
		}
		int ret2=0;
		if(idx-1>=0)
			ret2=arr2[idx-1];
		for(int i=pre;i<n;i++) {
			if(ret==arr[i]) {
				continue;
			}
			arr2[idx]=arr[i];
			ret=combination(idx+1,i);
			
		}
		return ret2;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String[] str= br.readLine().split(" ");
		n=Integer.parseInt(str[0]);
		m=Integer.parseInt(str[1]);
		arr=new int[n];
		arr2=new int[m];
		str= br.readLine().split(" ");
		for(int i=0;i<n;i++) {
			arr[i]=Integer.parseInt(str[i]);
		}
		Arrays.sort(arr);
		combination(0,0);
		
		br.close();
		pw.close();
	}
}
/*
8 4
1 1 1 1 2 2 2 2

1 1 1 1 
1 1 1 2 
1 1 2 1 
1 1 2 2 
1 2 1 1 
1 2 1 2 
1 2 2 1 
1 2 2 2 
2 1 1 1 
2 1 1 2 
2 1 2 1 
2 1 2 2 
2 2 1 1 
2 2 1 2 
2 2 2 1 
2 2 2 2 

*/


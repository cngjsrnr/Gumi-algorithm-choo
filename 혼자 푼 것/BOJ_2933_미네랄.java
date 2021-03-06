import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BOJ_2933_미네랄 {
	static char[][] map;
	static boolean[][] isvisited;
	static int r,c;
	
	static class Pair{
		int x;
		int y;
		public Pair() {}
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}	
		
	}
	static ArrayList<Pair> clust=new ArrayList<>();
	
	static int initIsvisited() {
		int cnt=0;
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				if(map[i][j]=='x') {
					cnt++;
					isvisited[i][j]=true;
				}
				else
					isvisited[i][j]=false;
			}
		}
		return cnt;
	}
	
	static void insertClust(int x,int y) {
		if(x<0 || x>=r || y<0||y>=c)
			return;
		if(!isvisited[x][y]) {
			return;
		}
		clust.add(new Pair(x,y));
		isvisited[x][y]=false;
		insertClust(x-1, y);
		insertClust(x+1, y);
		insertClust(x, y-1);
		insertClust(x, y+1);		
	}
	
	static int getFallLength() {//얼마나 움직여야되는지 클러스터로 각 열의 제일 낮은애로 확인
		int move=Integer.MAX_VALUE;
		Collections.sort(clust,new Comparator<Pair>() {
			@Override
			public int compare(Pair o1, Pair o2) {
				if(o1.y==o2.y) {
					return -(o1.x-o2.x);
				}
				return o1.y-o2.y;
			}
		});
		
		//클러스터의 한 열에서 가장 작은 행의 애들 얼만큼 움직일수잇는지 계산해
		for(int i=0;i<clust.size();i++) {
			Pair now= clust.get(i);
			int idx=0;
			if(i==0) {
//				if(now.x+idx+1<r) {
					while(map[now.x+idx+1][now.y]=='.') {
						idx++;
						if(now.x+idx+1>=r)
							break;
					}
					move=move>idx?idx:move;
//				}
				continue;
			}
			//같은 열인경우 continue;
			if(clust.get(i).y==clust.get(i-1).y)
				continue;
			else {
//				if(now.x+idx+1<r) {
					while(map[now.x+idx+1][now.y]=='.') {
						idx++;
						if(now.x+idx+1>=r)
							break;
					}
					move=move>idx?idx:move;
//				}
			}
		}
		
		return move;
	}
	
	static void fall(int x,int y) {
		//x,y좌표랑 연결된 미네랄들 다 떨어트려야함

		//clust에 애들 집어넣어
		initIsvisited();
		insertClust(x,y);
		//일단 몇칸 움직일지 찾아내
		initIsvisited();
		//얼마나 움직여야되는지 찾아
		int move=getFallLength();
		
		//이동
		Collections.sort(clust,(a,b)->-(a.x-b.x));
		for(Pair tmp:clust) {
			map[tmp.x][tmp.y]='.';
			map[tmp.x+move][tmp.y]='x';			
		}	
	}
	
	//dfs로 붙어잇는애들 갯수 새서 전체갯수랑 비교
	static int isFlyDfs(int x,int y) {
		if(x<0 || x>=r || y<0||y>=c)
			return 0;
		if(!isvisited[x][y]) {
			return 0;
		}
		int cnt=1;
		isvisited[x][y]=false;
		
		if(x==r-1)//맨 밑경우가 있으면 결과가 -리턴되게 이렇게 함
			return -11000;
		
		cnt+=isFlyDfs(x-1, y);
		cnt+=isFlyDfs(x+1, y);
		cnt+=isFlyDfs(x, y-1);
		cnt+=isFlyDfs(x, y+1);
		
		
		return cnt;
	}
	
	//떨어지는것도 한쪽이 떨어져도 다른쪽은 붙어있을수도 있음 그래서 dfs
	static boolean isFly(int x, int y) {
		if(x<0 || x>=r || y<0||y>=c)
			return false;
	
		//isFly DFS
		int mineralNum=initIsvisited();
		int mineralCnt=isFlyDfs(x, y);
		if(mineralCnt<0)
			return false;
		if(mineralCnt==0)
			return false;
		if(mineralCnt==mineralNum) {//한영역 미네랄갯수, 전체미네랄갯수 같으면 안떠잇는거임
			return false;
		}else {
			clust.clear();
			return true;
		}
	}
	
	static void mineral(int h,int direction) {//밑에부터가 0임, 0왼쪽 1오른쪽
		int idx=r-h;//배열에 맞는 인덱스로 변환
		int j=0;
		for(int i=0;i<c;i++) {
			if(direction==0)
				j=i;
			else
				j=c-i-1;
			if(map[idx][j]=='x') {
				map[idx][j]='.';
				//이제 없앤애 기준 상하좌우 애들이 공중에 떠잇나 확인
				if(isFly(idx-1,j)) {
					fall(idx-1,j);
				}
				else if(isFly(idx+1,j)) {
					fall(idx+1,j);
				}
				else if(isFly(idx,j+1)) {
					fall(idx,j+1);
				}
				else if(isFly(idx,j-1)) {
					fall(idx,j-1);
				}
				return;
			}
		}	
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw= new PrintWriter(System.out);
		
		
		String str;
		String[] strarr;
		int n;
		strarr=br.readLine().split(" ");
		r=Integer.parseInt(strarr[0]);
		c=Integer.parseInt(strarr[1]);
	
		//input
		map=new char[r][c];
		isvisited=new boolean[r][c];
		for(int i=0;i<r;i++) {
			str=br.readLine();
			for(int j=0;j<c;j++) {
				map[i][j]=str.charAt(j);
			}
		}
		
		n=Integer.parseInt(br.readLine());
		
		strarr=br.readLine().split(" ");
		for(int i=0;i<n;i++) {
			int now=Integer.parseInt(strarr[i]);
			mineral(now,i%2);
		}
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				pw.print(map[i][j]);
			}
			pw.print("\n");
		}
		pw.close();
		br.close();		
	}
}
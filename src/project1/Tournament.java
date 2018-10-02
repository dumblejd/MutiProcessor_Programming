package project1;

public class Tournament {

	private int maxlevel;
	private Peterson [][]tree;
	Tournament(int thread_num) //will not use request number here
	{
		maxlevel=(int)Math.ceil(((Math.log(thread_num))/Math.log(2)));
		tree=new Peterson[maxlevel+1][thread_num+1]; // will not use every one for each level 
		for(int i=0;i<tree.length;i++)
		{
			for(int j=0;j<tree[i].length;j++)
			{
				tree[i][j] = new Peterson();
			}
		}
	}
	public void lock(int id)
	{
		int level=maxlevel-1;
		//lock from bottom to top
		while(level>=0)
		{
			int up=id/2;
			tree[level][up].lock(id);
			id=up;
			level--;
		}
	}
	public void unlock(int id)
	{
		//unlock from top to bottom
		int copyid=id;
		//record the way
		int []path=new int[maxlevel+1];
		for(int i=path.length-1;i>0;i--)
		{
			path[i]=id;
			id/=2;
		}
		//top down
		int level=0;
		for(int i=0;i<path.length-1;i++)
		{
			tree[level][path[i]].unlock(path[i+1]);
			level++;
		}
//		//unlock from BOTTOM to TOP  will fail because one of the side will allow below both to get in 
//		int level=maxlevel-1;
//		//lock from bottom to top
//		while(level>=0)
//		{
//			int up=id/2;
//			tree[level][up].unlock(id);
//			id=up;
//			level--;
//		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

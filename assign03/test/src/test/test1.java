package test;
import java.util.*;
public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner s = new Scanner(System.in);
		int[] list = new int[10];
		int i = 0;
		while(i<10)
		{
			list[i] = s.nextInt();
			i++;
		}
		int length = list.length;
		System.out.print(biggest(list,length));

	}
	public static int biggest(int[] list, int length)
	{
		if(length ==1)
			return list[0];
		else if(length ==2)
		{
			if(list[length-1]>list[length-2])
				return list[length-1];
			else
				return list[length-2];
		}
		else
		{
			int num1 = list[length-1];
			int num2 = list[length-2];
			if(num1>num2)
				list[length-2] = num1;
			length--;
			return biggest(list,length);
		}
	}

}

import java.io.*;
import java.nio.file.*;
public class WriteHTML
{
	File file;
	BufferedWriter bufferedWriter;
	
	public WriteHTML()
	{
		try
		{
			file=new File("/var/www/html/test.html");
			bufferedWriter=new BufferedWriter(new FileWriter(file));
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public void writeHead()
	{
		try
		{
			String head_html="<!doctype html><html><head><title>beacon</title></head><body>";
			bufferedWriter.write(head_html);
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public void writeTail()
	{
		try
		{
			String tail_html="  </body></html>";
			 bufferedWriter.write(tail_html);
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public void closeBufWriter()
	{
		try
		{
			bufferedWriter.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public void writeBody(String message)
	{
		try
		{
			bufferedWriter.newLine();
			bufferedWriter.write(message);
			bufferedWriter.newLine();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
}

package pegdown;

import org.pegdown.PegDownProcessor;

public class PSample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PegDownProcessor pegDown = new PegDownProcessor(); 
		String markDownRawString = "neha _here_";
        String htmlString = pegDown.markdownToHtml(markDownRawString);
        System.out.println(htmlString); 
		
	}

}

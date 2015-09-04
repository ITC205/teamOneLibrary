package library.interfaces.hardware;

public interface IScanner {
	
	public void addListener(IScannerListener listener);
	
	public void setEnabled(boolean enabled);

}


import acm.graphics.GImage;
import acm.graphics.GRectangle;

/**
 * GameObject can also be seen as an GImage
 * @author elmar
 *
 */
public class GameObject extends GImage {
	
	private int x, y;
	
	public GameObject(String img, int x, int y) {
		super(img);
		
		this.x = x;
		this.y = y;
		
		setLocation(x, y);
	}
	
	public GameObject(GameObject obj) {
		super(obj.getImage());
		this.x = (int) obj.getX();
		this.y = (int) obj.getY();	
	}

	public void move(int dx,int dy) {
		super.move(dx,dy);
	}
	
}

package org.dl4j.jdbc;
import java.util.List;
import javax.sql.DataSource;
public interface ImageDao {
	public void setdatasource(DataSource ds);
	public void addImage(Image image);
	public List<Image> getLast10();

}

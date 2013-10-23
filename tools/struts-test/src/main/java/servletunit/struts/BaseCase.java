package servletunit.struts;

public class BaseCase
{
    public BaseCase() {
        super();
    }

    public BaseCase(String s) {
        super();
    }

    public void assertEquals(Object expected, Object actual)
    {
	if(expected.equals(actual)) return;

	throw new RuntimeException("Expected ["+expected+"] got ["+actual+"]");
    }

    public void assertEquals(String msg, Object expected, Object actual)
    {
	if(expected.equals(actual)) return;

	throw new RuntimeException(msg+". Expected ["+expected+"] got ["+actual+"]");
    }

    public void assertTrue(String msg, boolean actual)
    {
	if(actual) return;

	throw new RuntimeException(msg);
    }

    public void assertTrue(boolean actual)
    {
	if(actual) return;

	throw new RuntimeException("Not true");
    }


    public void assertNull(Object actual)
    {
	if(actual != null){
	    throw new RuntimeException("Object was not null ["+actual+"]");
	}
    }

    public void assertNotNull(Object actual)
    {
	if(actual == null){
	    throw new RuntimeException("Object was null");
	}
    }


    public void assertNull(String msg, Object actual)
    {
	if(actual != null){
	    throw new RuntimeException(msg);
	}
    }

    public void assertNotNull(String msg, Object actual)
    {
	if(actual == null){
	    throw new RuntimeException(msg);
	}
    }

    public void fail(String msg)
    {
	throw new RuntimeException(msg);
    }

    public void fail()
    {
	throw new RuntimeException("Test failed");
    }

}
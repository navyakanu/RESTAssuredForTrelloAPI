//
//@BeforeSuite
//@Parameters({"env"})
//public void setUp(String environment) throws IOException {
//        // public void setUp() throws IOException {
//        PropertiesLoader.LoadProperties();
//
//        if (environment.equalsIgnoreCase("testenv")) {
//        System.out.println("*****"+ environment);
//        RestAssured.baseURI = PropertiesLoader.prop.getProperty("url");
//        key = PropertiesLoader.prop.getProperty("key");
//        token = PropertiesLoader.prop.getProperty("token");
//
//        }
//        else
//        {
//        RestAssured.baseURI = PropertiesLoader.prop.getProperty("mockurl");
//        key = PropertiesLoader.prop.getProperty("mockkey");
//        token = PropertiesLoader.prop.getProperty("mocktoken");
//        }
//        expectedBoardID = createNewBoard();
//        }
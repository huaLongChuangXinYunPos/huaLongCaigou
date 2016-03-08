package Tool;

public class Config {
	
	public static String Updateuser="http://192.168.3.199:1234/AppServer/GetUserInf.aspx?action=RQAll"; //更新采购员
    public static String Updategoods="http://192.168.3.199:1234/AppServer/GetGoodsList.aspx?action=RQAll";//更新商品
    public static String Supplier="http://192.168.3.199:1234/AppServer/GetSupplier.aspx?action=RQAll";
    public static String t_CheckTast="http://192.168.3.199:1234/Appserver/GetCheckList.aspx?action=RQCK";
    public static String t_CheckTast_zone="http://192.168.3.199:1234/Appserver/GetCheckList.aspx?action=RQCKZ";
    public static String t_WareHouse="http://192.168.3.199:1234/AppServer/GetGoodsList.aspx?action=RQCK";  //仓库



   public static String commitpurchase="http://192.168.3.199:1234/APPWebService.asmx/RetStockDetail";
   public static String commitcheck="http://192.168.3.199:1234/APPWebService.asmx/RetCheck";
   public static String commitxamineGoods="http://192.168.3.199:1234/APPWebService.asmx/RetStockVer"; //验货 ，name=json
}

package communicationModel.addressModel;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public class AddressMac extends Address{
    public static String AddressType = "AddressMac";
    private static int AddressIndex = 0;
    protected AddressMac(){
        super(AddressType);
        this.addressValue = AddressIndex++;
    }
}

package communicationModel.addressModel;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public abstract class Address extends AddressBase{
    private static String AddressType = "Address";
    private static int AddressIndex = 0;
    protected int addressValue;
    protected String addressType;

    private Address(){
        this.addressType = AddressType;
        this.addressValue = AddressIndex++;
    }

    protected Address(String addressType){
        this.addressType = addressType;
    }

    final public static AddressMac allocateAddressMac(){
        return new AddressMac();
    }

    @Override
    String getAddressType() {
        return this.addressType;
    }

    @Override
    int getAddressValue() {
        return this.addressValue;
    }

    /**
     * 判断两个地址是否相等
     * @param address 地址
     * @return 是否相等
     */
    public boolean isEqual(Address address){
        if (this.addressType.equals(address.addressType)
                && (this.addressValue == address.addressValue)){
            return true;
        }
        else {
            return false;
        }
    }

}

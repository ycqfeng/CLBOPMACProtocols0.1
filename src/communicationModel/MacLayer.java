package communicationModel;

import communicationModel.addressModel.Address;
import communicationModel.addressModel.AddressMac;
import simulator.BaseObject;

/**
 * Created by ycqfeng on 2017/3/28.
 */
public abstract class MacLayer extends BaseObject{
    private static int index = 0;
    private int uid;
    private AddressMac addressMac;

    protected MacLayer() {
        this.uid = index++;
        this.addressMac = Address.allocateAddressMac();
    }

    public AddressMac getAddressMac(){
        return this.addressMac;
    }

    @Override
    public int getUid() {
        return this.uid;
    }
}

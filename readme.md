# What's this
The BluetoothClient class is helps maintain connection with other bluetooth device like Arduino.
It has a robust reconnecting logic to ensure the app constantly connecting to the target device, and keep trying reconnecting with the target device when the connection is lost

# Prerequisite:
Need to pair with the target bluetooth device first.
## Paring free alternative
The mac branch manages the connection by MAC address of the target device. It scans and remember the target device's address, and do connection/reconnection by it.

However when establishing the connection with the device, if it's not paired already, a confirm dialog will show and ask for permission for pairing. So this method is not exactly pairing free.
### TODO:
Test the behaviour when target arduino.

# Features
## Connection management:
* keep trying connecting to the target device until connection is made.This suvives events like reboot of the target device, wireless interference, and the device going out of range. Once the target device is ready again, with BluetoothClient, the app can automatically reconnect with the target device.
* It notifies the caller about bluetooth disconnection happens. The caller can then inform the BluetoothClient to connect again.
## Communication
* Send command string to the target device; auto add new line '\n' as delimiter.
* Receive response message from the target device; auto seperate messages with '\n' as delimiter.

# Usage
## basic
```
bluetoothClient = new BluetoothClient(handler, targetDeviceName)
bluetoothClient.connect()
```
## Ensure reconnecting:
### Get reliable notification about bluetooth disconnect
the BluetoothClient class itself can not detect socket disconnect in time, the caller can listen to system broadcast about the disconnection. 

BluetoothClient class offers a getBluetoothBroadcastReceiver() along with a safely register and unregister method, which the caller can use to listen to system broadcast.

### when to call connect?
* at lifecycle event pair: onStart -- onStop / onResume-- onPause
* at connection lost event. When the BluetoothClient is hooked up correctly with the ACTION_ACL_DISCONNECTED intent filter, the handler will receive MESSAGE_CONNECTION_LOST message when the connection is lost.

# Potential problem:
Sometimes the target device does not detect disconnection properly that it won't accept any new connection request even if the old connection is already lost, need to make sure the target device doesn't do this or auto reconnect will not work without reset.

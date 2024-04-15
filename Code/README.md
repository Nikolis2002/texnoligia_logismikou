--guide gia ta paidia apo nikoli :)--
--loipon o kwdikas kai to uml einai ftiagmeno wste na douleuete me tis pio genikes klaseis kai me interfaces--
Paradeigma:
classes:
Transport
Car
interface:
Gas
---------------------example-------------------------------------
class Car extends Transport implements Gas.
---------------------Sthn main------------------------------------
Transport modeo=new Car(....);
Gas modeoKaiei = (Gas) modeo;
modeo.getGas();
-------------------the end------------------------------------------
me auto ton tropo tha pairnete apo thn genikh klash leptomereies pou kanei implement ena paidi ths ;),
mporeite na kanete kai aplo downcast alla autos o tropos einai pio clean ;)


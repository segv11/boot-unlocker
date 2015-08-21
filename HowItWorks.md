## BootUnlocker for Nexus Devices -- Unlock your bootloader without fastboot. ##

This application **REQUIRES** a Galaxy Nexus (_maguro_, _toro_ or _toroplus_), Nexus 4 (_mako_), Nexus 5 (_hammerhead_),
Nexus 7 2013 (_deb_ or _flo_), Nexus 10 (_manta_),
or OnePlus One (_bacon_ / _A0001_), with root.


---


_BootUnlocker for Nexus Devices_ avoids using `fastboot oem unlock`, with its associated
`userdata` wipe. When fastboot unlocks it updates a lock status flag, stored on a partition
of your device's internal storage. Device partitions, positions and state values
(locked/unlocked) are as follows:

  * On the Galaxy Nexus, the bootloader uses at position `0x000007C` of the "param" partition, stored as 01 / 00.
  * On the Nexus 10, the bootloader uses position `0x0000224` of the "param" partition, stored as 00 / 01.
  * On the Nexus 4 and Nexus 5, the bootloaders use position `0x0004010` of the "misc" partition, stored as 00 / 01.
  * On the Nexus 7 (2013), the bootloader uses position `0x04FFC00` of the "aboot" partition, stored as 00 / 02.
  * On the OnePlus One, the bootloader uses position `0x000FFE10` of the "aboot" partition, stored as 00 / 01.

The Nexus 4 and Nexus 5 bootloaders also keep a "Tamper" flag at position `0x0004014` of the "misc" partition.
It is stored as 00 / 01 (untampered/tampered), and can be viewed using `fastboot oem device-info`.
The OnePlus One has a "Tamper" flag, at position `0x000FFE14` of the "aboot" partition.
_BootUnlocker for Nexus Devices_ can set and clear this flag too.


_BootUnlocker_ uses root privileges to write to to the appropriate location directly, bypassing fastboot.
This allows you to lock and unlock your bootloader from within Android, without wiping your `userdata` partition.

The technique used was discovered through the efforts of several contributors on http://forum.xda-developers.com/showthread.php?t=1650830&page=13

Special thanks go to those who posted raw images of their device partitions,
helped with/conducted the analysis, or put their devices in harm's way to beta test:
efrant, osm0sis, iuss, Archpope, AdamOutler, NCguy, Raftysworld, Mach3.2, Meep70, Polarfuchs, and others.
This application could not have been written without their contributions.

To learn more about how this app works, and plans for future functionality, follow this project on Google Code,
or subscribe the application's XDA thread: http://bit.ly/BootUnlocker
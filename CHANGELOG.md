# Release 1.0-beta22

## Core
* Negative flow rates are now allowed for gate filling types.
* New time step is computed is computed from either upstream or downstream water depth, depending on which one is greater.
* Removed check for negative upwind parameter.
* New default values for jet parameters. 
* Fixed missing per mille sign in ratio of ship force to gravitational force in model output.
* Code improvements.

## GUI
* Plotting speed is heavily optimized by thinning out results data

## Other
* Better understandable column headers in exported result files (WARNING: destroys backward compatibility when loading old result files for comparison).
* Improved handling of invalid values in result files when loaded for comparison.


# Release 1.0-beta21

## Core
* Overlapping sources in prescribed inflow type are now summed up correctly.

## GUI
* Fixed a bug in the line colors of comparison data.

## Other
* Added tar.gz archive format for releases.
* 3rd party licenses were not copied into release file.


# Release 1.0-beta20

## Core
* Removed generic sluice gate.
* Added generic gate.
* Width of sluice gate can be defined dependent on opening height.
* New default values.

## GUI
* It is now possible to start the GUI with a given case file name.
* New category names.
* Improved speed of plotting the results.

## Other
* New Case version 0.8 (WARNING: destroys backward compatibility!).
* Fixed spelling bugs.


# Release 1.0-beta19

## Core
* Added possibility to suppress text output of OneDimensionalModel.
* New jet cross section computation.

## GUI
* New license section in 'About' dialog

## Other
* LoMo is now a Maven project
* Prepared GitHub release
* RELEASE file is now called CHANGELOG
* New Case version 0.7.


# Release 1.0-beta18

## Core
* Improved computation of jet cross section for gate filling types. jetOutlet + jetCoefficient * Math.pow(position, jetExponent) * jetOutlet  
  WARNING: This will change the results of all old cases!

## GUI
n/a

## Other
* New format for the RELEASE files. 


# Release 1.0-beta17

## Core
* Major refactoring of filling types
* New generic sluice gate filling type
* New definition of jet outlet
* Removed culvert influence

## GUI
* Adjustments in Messages

## Other
* Added XML Schema file for BAWLomoCase


# Release 1.0-beta16

## Core
* Override getJetCrossSection method in SluiceGateFillingType to account
for slow opening of sluice. The jet cross section is computed as minimum of given culvert cross section and actual opening cross section of the sluice gate.
* Introduced additional segment gate with prescribed opening velocity.

## GUI
n/a

## Other
n/a


# Release 1.0-beta15

## Core
n/a

## GUI
* Added extension filter for all files.
* Switched off animation for charts.
* Added possibility to compare with a snapshot of the current results.
* PopOverEditors can be now dragged to a second screen.
* Updated version of controlsfx and path seperator in linux shell files.

## Other
n/a


# Release 1.0-beta14

## Core
n/a

## GUI
* Added possibility to show comparison data.
* Improved PopOverKeyValueListPropertyEditor. Automatic sorting.

## Other
* Usage of lambda functions in GUI classes.


# Release 1.0-beta13

## Core
* Internationalization of model results output. 
* Fixed bug in computation of Fx/Fg
* Default value for culvert cross section in SegmentGateFillingType changed to 999999
* Standardized names of methods and localization tags.
* BAWLomoCase XML version is now 0.5 (due to breaking changes!)

## GUI
* Tagging exported images and data with current date, version, author and
description.
* Improved english localization
* Moved field maximumPressureHead below discharge coeffcient table in GUI.
* Fixed bugs in PopOverKeyValueListPropertyEditor

## Other
n/a


# Release 1.0-beta12

## Core
n/a

## GUI
* Added parameter help

## Other
n/a

# Release 1.0-beta11

## Core
* Adjusted precision in text output of model.

## GUI
* Fixed bug in scaling of axes.
* Fixed bug when entering an invalid value in table cell, fixed bug when
deleting all table rows.
* Updated controlsfx library to 8.40.12

## Other
* Added schemagen batch script.


# Release 1.0-beta10

## Core
* Default end value of sluiceGateHeightLookup is set to 1000,

## GUI
* Table cells can now handly commas and non-digit values. Initialization
of visualData is done right, when modifying data array, e.g. when
loading Flowmaster data.
* Fixed bug when computation was not possible again after an error occured.
* All file open/save operations do remember the last used directory.
* Improved design of legend.
* Added property charts. One instance of popover only.
* Minor code improvement
* Calc button has its own onAction method now.
* Fixed bug in scaling of axes.

## Other
n/a
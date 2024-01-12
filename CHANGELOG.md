# Release 2.0-beta6

## Core
* New method 'getPositions' to retrieve node positions from results
* Results now include the opening over time for all filling elements. Therefore, the return type of 'getValveOpeningOverTime' method was changed.
* Water level result at time step zero is now initialized with downstream water level (before it was set to zero)
* Default ship length set to 135 m (before it was 130 m)
* Default maximum simulation time set to 3600 s (before it was 1000 s)
* Ship force is no longer displayed as absolute value but with a sign

## GUI
* Show opening over time for all filling elements in the chart
* Export/import results with opening over time for all filling elements
* Round output of SavingLockDesigner to two decimal places

## Other
n/a


# Release 2.0-beta5

## Core
n/a

## GUI
* Show two decimal places for longitudinal force and slope

## Other
* It is now possible to access the LoMo API inside jlink image with JPype (Python)
* Refactor code using results from PMD source code analyzer
* Update dependencies

## Known bugs and flaws
* The valve opening line in the chart is not yet optimized for multiple filling elements.
* There are no consistent default values for the saving basins yet.
* No rounding of values set by saving lock design assistant.


# Release 2.0-beta4

## Core
* Results now include water level and flow velocity over space and time
* It is now possible to define bounds for ship force computation

## GUI
* Possibility to enter bounds for ship force computation

## Other
* Releases are build using Eclipse Temurin OpenJDK

## Known bugs and flaws
* The valve opening line in the chart is not yet optimized for multiple filling elements.
* There are no consistent default values for the saving basins yet.
* No rounding of values set by saving lock design assistant.


# Release 2.0-beta3

## Core
n/a

## GUI
* Fixed bug in filling type selector which appeared if the currently selected filling type is selected again in the list.
* Added tooltips to the saving lock design assistant.

## Other
n/a

## Known bugs and flaws
* The valve opening line in the chart is not yet optimized for multiple filling elements.
* There are no consistent default values for the saving basins yet.
* No rounding of values set by saving lock design assistant.


# Release 2.0-beta2

## Core
* More than one filling type can be added to a case!
* New filling type representing a saving basin.

## GUI
* New 'Edit' menu which allows to add, copy and delete filling types.
* New dropdown in the filling type properties to select the current filling type and to change its name.
* New assistant for designing a saving lock in 'Help' menu.
* Added possibility to change language in 'Settings' menu.

## Other
* Added SavingLockDesigner in the utils.
* Added possibility to deep copy data objects in the IOUtils.
* Filling types are now loaded with ServiceLoader.
* Filling types now have a name field.

## Known bugs and flaws
* The valve opening line in the chart is not yet optimized for multiple filling elements.
* There are no consistent default values for the saving basins yet.
* No rounding of values set by saving lock design assistant.


# Release 2.0-beta1

## Core
n/a

## GUI
n/a

## Other
* Migrated to Java JDK 11+
* Migrated to Gradle Build Tool
* Release binaries have no more system requirements
* Release binaries are now platform dependent


# Release 1.1

## Core
n/a

## GUI
* Considered new URL for ControlsFX library.

## Other
* Discharge results can now be exported to BAW OpenFOAM 'timeVaryingFlowRateVelocity' 
boundary condition file. 
* Considered truncated result arrays in IOUtils.


# Release 1.1-beta2

## Core
* Improved file format for custom sources.
* Result arrays are now truncated to avoid empty entries.
* New constructor for CustomSource to allow initialization of fields.

## GUI
* Improved error handling.

## Other
* LoMo can be used with OpenJDK 1.8 from ojdkbuild project as an replacement 
for Oracle Java Runtime Environment 8. You have to select the JavaFX feature 
in the MSI installer.


# Release 1.1-beta1

## Core
* An error in the determination of the cell midpoints was fixed. These positions
were only used in the prescribed inflow filling type so far.
* The prescribed inflow filling type was replaced by the new custom source 
filling types.

## GUI
n/a

## Other
* Due to the changes in the filling type the XML case version was updated to 0.9. 
To load XML case files from version 0.8, make sure you have not used the 
`prescribedInflowType` element and replace the `version` attribute of the 
`BAWLomoCase` element with 0.9 in a text editor.


# Release 1.0

## Core
n/a

## GUI
* Added link to online help
* Improved descriptions
* Added minimum flow rate to console output
* Clean reset when selecting File>New

## Other
* Renamed column header 'inflow' to 'flow\_rate' for consistency 
(WARNING: destroys backward compatibility when loading old result files for 
comparison. Workaround: Manually rename column 'inflow' to 'flow_rate' in older 
result files).
* Brand-new wiki on github.com with information on getting started and some 
features of LoMo. (work in progress)


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
# LoMo

**LoMo** is an open source tool for analyzing lock filling processes. It uses an 
one-dimensional shallow water model to simulate the wave propagation in the lock
chamber. Beside the filling time, the forces acting on the ship in longitudinal
direction are computed. 


## Installation

**LoMo** can be run using our [released runtime images](https://github.com/baw-de/lomo/releases). Download and extract
the ZIP or TAR.GZ archive depending on your operating system. On Windows you can execute `bin\lomo.bat` with a double
click. On a UN*X system execute `bin/lomo`.

To build **LoMo** from source a Java JDK 11+ is required. Make sure the environment variable`JAVA_HOME` is pointing to 
your JDK. **LoMo** is build and run using the [Gradle Build Tool](https://gradle.org/):

```
git clone https://github.com/baw-de/lomo.git
cd lomo
git checkout -t <TAG>
gradlew.bat run        # Windows
./gradlew run          # UN*X systems
```
After installing **LoMo** you can continue reading our information on [getting started](https://github.com/baw-de/lomo/wiki/Getting-started).


## Development

**LoMo** is developed at the [Federal Waterways Engineering and Research Institute](http://www.baw.de/).
It is based on an object-oriented software design to allow simple extension of 
the software. 

**LoMo** is written in the Java programming language. Developing **LoMo** using 
 [IntelliJ IDEA](https://www.jetbrains.com/idea/) should work well.

You are welcome to submit issues or pull requests on [GitHub](https://github.com/baw-de/lomo/) 
to support the development of **LoMo**.


## Publications

The numerical core is described in detail in this paper, which we kindly ask you to cite if you are using **LoMo**:  
Belzner, F., Simons, F. & Thorenz, C. (2018): 'An application-oriented model for lock filling processes',
Proceedings of the 34th PIANC World Congress 2018, Panama. [PDF](https://coms.events/pianc-panama/data/full_papers/full_paper_183.pdf)


## Further information

* [Changelog](CHANGELOG.md)
* [Wiki](https://github.com/baw-de/lomo/wiki)


## License 

**LoMo** is distributed by the [Federal Waterways Engineering and Research Institute](http://www.baw.de/) 
and is freely available and open source, licensed under the 
[GNU General Public License 3](https://www.gnu.org/licenses/gpl.html). 
See [LICENSE.txt](LICENSE.txt) for details.

**LoMo** runtime images include the following third party libraries which are distributed under 
their own terms. See [3RD-PARTY.txt](3RD-PARTY.txt) for details.

* [Eclipse Temurin OpenJDK](https://adoptium.net/): GNU General Public License 2, with the Classpath Exception
* [JavaFX](https://openjfx.io/): GNU General Public License 2, with the Classpath Exception
* [ControlsFX](https://github.com/controlsfx/controlsfx): 3-Clause BSD License
* [Jakarta XML Binding](https://github.com/eclipse-ee4j/jaxb-api): Eclipse Distribution License (EDL) v1.0
* [Eclipse Implementation of JAXB](https://github.com/eclipse-ee4j/jaxb-ri): Eclipse Distribution License (EDL) v1.0


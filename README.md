# LoMo

**LoMo** is an open source tool for analyzing lock filling processes. It uses an 
one-dimensional shallow water model to simulate the wave propagation in the lock
chamber. Beside the filling time, the forces acting on the ship in longitudinal
direction are computed. 


## System requirements

* [Java Runtime Environment 8](https://java.com) 
* Java SE Development Kit 8 (optional)
* Apache Maven 3.3+ (optional)


## Installation

**LoMo** can be run using our [release binaries](https://github.com/baw-de/lomo/releases) or building it from source 
using [Apache Maven](https://maven.apache.org/).

```
git checkout -t <TAG>
mvn clean package
```


## Development

**LoMo** is developed at the [Federal Waterways Engineering and Research Institute](http://www.baw.de/).
It is based on an object-oriented software design to allow simple extension of 
the software. 

**LoMo** is written in the Java programming language. Developing **LoMo** using 
the [Eclipse IDE](http://www.eclipse.org/) should work well.

You are welcome to submit issues or pull requests on [GitHub](https://github.com/baw-de/lomo/) 
to support the development of **LoMo**.


## Publications

The numerical core is described in detail in this paper, which we kindly ask you to cite if you are using **LoMo**:  
Belzner, F., Simons, F. & Thorenz, C. (in press): 'An application-oriented model for lock filling processes',
Proceedings of the 34th PIANC World Congress 2018, Panama.


## Further information

* [Changelog](CHANGELOG.md)


## License 

**LoMo** is distributed by the [Federal Waterways Engineering and Research Institute](http://www.baw.de/) 
and is freely available and open source, licensed under the 
[GNU General Public License 3](https://www.gnu.org/licenses/gpl.html). 
See [LICENSE.txt](LICENSE.txt) for details.

**LoMo** uses the following third party libraries which are distributed under 
their own terms. See [LICENSE-3RD-PARTY.txt](LICENSE-3RD-PARTY.txt) for details.

• [ControlsFX](http://fxexperience.com/controlsfx): 3-Clause BSD License



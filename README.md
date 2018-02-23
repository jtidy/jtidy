# JTidy
[![Build Status](https://img.shields.io/travis/jtidy/jtidy/master.svg?colorA=9977bb&style=plastic)](https://travis-ci.org/jtidy/jtidy)
[![Build Status](https://img.shields.io/shippable/5a39c2859b0aca0700da9a9c/master.svg?colorA=9977bb&style=plastic)](https://app.shippable.com/projects/5a39c2859b0aca0700da9a9c/)
[![Code Quality](https://sonarcloud.io/api/project_badges/measure?project=jtidy%3Ajtidy&metric=alert_status)](https://sonarcloud.io/dashboard?id=jtidy)

# About
Revival of the JTidy Project updated to work with HTML5 new tags. Along 
with new option to not remove unknown tags:

```java
Tidy tidy = new Tidy();
tidy.setDropProprietaryTags(false);
```

# History
JTidy seemed to fall into disrepair years ago and had not been updated. 
A few had forked it on Github.
[William L. Thomson Jr.](https://github.com/wltjr) came along and 
created a fork of others forks with a tag for his packaging needs as a 
dependency for JMeter. Then another came along,
[Dell Green](https://github.com/dellgreen) who noticed some issues, 
tests failing, and undertook fixing both.

Since the code belonged to neither, William decided to create a JTidy 
organization and revive the project via community support. Which you 
are welcome to join in. Eventually this should become the official new 
home for JTidy.

Thanks to all past authors and developers. Those of which who could be 
found on Github have been invited to join this project. Along with those 
that this repository was forked from.

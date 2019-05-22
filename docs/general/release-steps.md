# Release steps

This guide shows the steps required to update to version 0.4

## 1. Update NEWS.md

If NEWS.md has not been updated in a while do a 

    git log

and summarise the commits.

Add
``` 
Release 0.4 2019-05-22 ()
----------------------
``` 
   

## 1. Update pom.xml

Update the version number in pom.xml from: 

``` 
  <groupId>nikita</groupId>
  <artifactId>nikita-noark5-core</artifactId>
  <packaging>jar</packaging>
  <version>0.3</version>
```
to:
``` 
  <groupId>nikita</groupId>
  <artifactId>nikita-noark5-core</artifactId>
  <packaging>jar</packaging>
  <version>0.4</version>
```

## 2. Tag a new version

    git tag -a 0.4 -m "0.4 RELEASE" 
    git push --tags
    git push

## 3. Update NEWS.md with commit 

``` 
Release 0.4 2019-05-22 ()
----------------------
``` 
becomes e.g:
``` 
Release 0.4 2019-05-22 (2a803f1a203fc14921081d519ab13bc623116a14)
----------------------
``` 

## 4. Update description on gitlab
Go the the Project
[description](https://gitlab.com/OsloMet-ABI/nikita-noark5-core/edit)
and change the version number.


## 5. Send mail to mailing list

Inform mailing list nikita-noark@nuug.no about the new release.

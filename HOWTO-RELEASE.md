# How to release to Maven Central

## Settings

To prepare a release, you must have published a PGP signing key to a public key server like "keys.openpgp.org".

You need an account at https://issues.sonatype.org/ with user-name and password to upload to a staging repository for 
releasing to maven-central. This account must be linked to the JTidy project. Adding a new user can be done by filing 
a Jira ticket there. 

The Sonatype account must be set up in your Maven settings file `~/.m2/settings.xml`:

```
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-user-name</username>
      <password>your-password</password>
    </server>
  </servers>
</settings>
```

## Preparation

Create and switch to a release branch from master. If anything goes wrong, this makes it easy to roll back the 
changes without affecting the master branch.

Update the project page with an announcement of the new version. At least, update the version number in the 
Maven dependencies template on the home page.

## Tag the new version

A new version number must be assigned to the project and tagged in the repository. 

```
mvn release:clean release:prepare
```

## Release to maven-central

If the previous command completed without errors, the new release can be built and uploaded to maven-central.

```
mvn -P release release:perform
```

## Create release on the GitHub page

Merge your release branch back to the master branch to update the project home page. Create a new GitHub release,
upload the artifacts `jar`, `javadoc`, `sources`, and `test-sources` to the release and create a description for the
new release.

Check, whether the artifact has appeared in Maven-Central: https://repo.maven.apache.org/maven2/com/github/jtidy/jtidy/

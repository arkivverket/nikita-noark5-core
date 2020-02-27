# Nikita developer notes

## 2018-11-28

  - OData parsing

### OData parsing

We have added further support for OData including parent/child searches like 
the following:

    http://localhost/noark5v5/api/arkivstruktur/arkivdel/123456789/saksmappe?$top=2$skip=4$filter=tittel

We also included $count as a parameter for search, but it is unclear how the 
results should be formatted. $count is not even mentioned in the noark5v5 
interface standard.

Note! If you want to test OData from command-line, remember you have to 
URL-escape the URL.     

    curl -v  --header Accept:application/vnd.noark5-v4+json --header Content-Type:application/vnd.noark5-v4+json --2bcd-91e7-45d2-ab19-e020d5d9c49a" -X  GET http://localhost:8092/noark5v5/api/arkivstruktur/arkivdel/c45c6d1b-a108-4db3-8a60-a8f8fc41e798/saksmappe?%24top=2%24skip=4%24filter=tittel+eq+%27hello+goodbye%27

## 2018-08-03
  
  - GUI improvements
  - Support for x-forward-for
  - Login not set correctly in root of application
  - Something strange about CORS
 
### GUI improvements

### Support for x-forward-for
We will start to support x-forward-for when generating hateaos links. This
will make the core less reliant on hard coded addresses in core and should
make it easier when firing up the core behind a proxy. Updating this will 
take some time, but we have tested it on the root of the application.

If x-forward-for is not set on an incoming request, nikita will use the value 
assigned in the application.yml file hateoas.publicAddress

### Login from root of application
 A GET on the root of the application:
 
    curl  -v   --header Accept:application/vnd.noark5-v4+json  -X GET http://localhost:8092/noark5v5/  
    
will return the following js:
    
    {
      "links": [
       {
         "href": "http://localhost:8092/noark5v5/oauth",
         "rel": "http://nikita.arkivlab.no/noark5/v5/login/rfc6749/"
       }
      ]
    }

You can then login in as follows:

    curl -v -H 'Content-Type: application/json' -H 'Authorization: Basic bmlraXRhLWNsaWVudDpzZWNyZXQ=' -X POST "http://localhost:8092/noark5v5/oauth/token?grant_type=password&client_id=nikita-client&username=admin&password=password"


### Something strange about CORS
  
We have cleaned up and removed a lot of code over the last while so the
application should be relying as much as possible on the basic spring
boot setup. However I see that there are two CorsFilter in the application
and this is not a good thing. Our orginal CorsFilter:
  
    nikita.webapp.spring.SimpleCORSFilter
 
 and 
 
    nikita.webapp.spring.security.configs.authentication.NikitaWebSecurityConfig there 
    
contains a bean _corsFilterRegistrationBean_

       
For some reason the application did not work without both of them. SimpleCorsFilter
does not run unless corsFilterRegistrationBean also exists. This issue came 
about as a result of the implementation of OAuth2 and spring security.

Debugging the filter chain showed that the issue was related to priority ordering. 
I think the securityfilterchain was taking the request and giving a 401. So I 
had to get the SimpleCorsFilter above the securityfilterchain. With this in place
the code started working.

So I removed the following and now just have a single SimpleCorsFilter. Leaving this
here for documentation in case any issues pop up again.

        @Bean
        public FilterRegistrationBean corsFilterRegistrationBean() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.addAllowedOrigin("*");
            config.setAllowedMethods(Arrays.asList("OPTIONS", "POST", "GET", "PUT",
                    "PATCH", "DELETE"));
            config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin",
                    "Content-Type", "Accept", "Authorization", "ETAG"));
            source.registerCorsConfiguration("/**", config);
            FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
            bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return bean;
        }

The OPTIONS check is now returning before a security check. Is this OK? I 
believe so. But we do need to write a document discussing how this is
implemented.  
  
  
## 2018-07-29

  - Conversion process
 
### Conversion process

We've given this a little more thought. There probably needs to be an own 
application that can handle this. We should take a look at archivematica 
to see what they have done. A quick check mentioned a service that they
wanted to look closer at in the future so I think we will have a go at
this ourselves. I think it could be a RabbitMQ service where we pipeline
documents to be converted. Once a document reaches the head of the queue
it is sent to a conversion service. This could be a pixedit server for
OOXML-based documents and unoconv for ODF-based documents. Further
audio and video could also be included here. This tool could be further
developed to check validity of file formats as well.


## 2018-07-13

 - Spring security
 - Testing and documentation
 - Conversion process
 - Support for PATCH

### Spring security

We upgraded to the latest spring-security version and introduced support for
the following Authentication mechanisms:    
  
  - Basic authentication 
  - Form-based authentication
  - OAuth2
  - JWT via OAuth2

This is configured via profiles and the default set in the properties yml
files is to start with OAuth2 authentication. We have also been working on 
SAML integration but so far have not had any success wth that. Feide is meant
to be working on OAuth2 support so perhaps this is an issue that may go away.
We need to support SAML so we can allow students log in via the Feide SSO
system.  

### Testing and documentation

We have also changed the testing framework that it can produce documentation. 
This has been achieved by incorporating [spring-rest-docs](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/) 
in the project and the testing framework we develop will make use of this. As 
documentation isbased on tests, the documentation should not get stale. Any 
change that does not meet the test/documentation requirements should result in
a build failure. 

### Conversion process
We managed to get a conversion process in place for use in the
the core. This means that there is now a dependency on libreoffice
in the core. It only is required if you try to convert a file from a production
format to an archive format. It uses the [unoconv](https://github.com/dagwieers/unoconv) 
tool to undertake a conversion.

    unoconv -f pdf -o outputFile.pdf inputFile.odt

We are currently using the following approach in Java

    Process p = Runtime.getRuntime().exec(unoconcCommand);
    p.waitFor();

We tried using the UNO API with LibreOffice but ran into a number of problems.
Eventually based on the following [blog](https://technology.amis.nl/2006/07/19/getting-started-with-the-openofficeorg-api-part-ii-starting-openofficeorg-with-jars-not-in-the-ooo-install-dir/)
we saw that you had to have some LibreOffice jars from the LibreOffice 
installation on the classpath and we were able to connect to LibreOffice 
that way. This would have made an install of nikita very messy, having to 
update the pom file on each download. Therefore we dropped that approach
for the moment. We foresee that we require a queue system to handle the
conversion of files where perhaps a user requests the conversion and a 
conversion tool converts the file as soon as possible. This will have to 
be non-blocking in order to scale. So for the moment we have a "blocking"
approach using unoconv, but it's just proof-of-concept. 

### Support for JSON PATCH

We have implemented partial support for the PATCH approach to updates as 
defined in [RFC 5789](https://tools.ietf.org/html/rfc5789) and JSON PATCH
as defined in [RFC 6902](https://tools.ietf.org/html/rfc6902). We have
only implemented the "replace" command in order to test the approach out.


 
## 2018-03-20

 - Project structure
 - Continued OData
 - Testing framework
 - Upgrade to spring-boot 2

### Project structure

We changed the project from a multi-module maven project to a single module 
maven project. The reason for this is that we were never able to get spring 
working "just right" as a multi-module project. We weren't for example able to 
get spring testing working properly. We missed out on a lot of springs 
autoconfiguration.

With this change we got rid of a number of annotations and now let spring 
automagically configure everything.

We keep the webapp separate from common logic using package names, so if anyone 
wants to reuse a lot of of the ORM layer it should be possible to take it 
from nikita.common.

We used this opportunity to change the package naming as well.  The old 
naming was no.arkivlab.hioa.nikita. This naming came from an initiative to try 
to create a community for public archives software called arkivlab. Nothing 
ever came of this initiative and the project is firmly anchored at OsloMet 
(earlier HiOA) so the need for package naming to include organisation is 
redundant. HiOA is now called OsloMet so removing hioa from the codebase 
makes sense.

### Continued OData
OData support continues. The code is now able to create simple SQL/HQL 
statements from simple OData syntax. The following examples are supported:

    http://localhost/noark5v5/api/arkivstruktur/arkiv?$filter=contains(tittel, 'Oslo') and tittel eq 'goodbye'$top=2$skip=4$orderby=tittel desc
    http://localhost/noark5v5/api/arkivstruktur/arkiv?$filter=tittel eq 'hello'$top=2$orderby=tittel desc
    http://localhost/noark5v5/api/arkivstruktur/arkiv?$top=2$skip=4$filter=tittel eq 'hello'$orderby=beskrivelse desc
    http://localhost/noark5v5/api/arkivstruktur/mappe?$filter=tittel lt 'hello'
    http://localhost/noark5v5/api/arkivstruktur/arkiv?$filter=startsWith(tittel, 'hello')
    http://localhost/noark5v5/api/arkivstruktur/arkiv?$filter=contains(beskrivelse, 'hello')
    http://localhost/noark5v5/api/arkivstruktur/mappe?$filter=tittel eq 'hello'

There is still a bit more work to be done and quality control. But this is a 
good start. Going forward, we will integrate this to the controller, service and
persistence layers. There is an ongoing clean-up of the controller / service 
layers so we will gradually introduce this for all Noark entities.

### Testing framework 

The Testing framework has definitely been one of this project achilles heel. 
I was never able to get a testing framework to work, partly because I have no
experience with testing, our project structure was such that I could not get 
started and I did not really see the difference between unit tests and 
integration tests.

This has also resulted in our approach to CI being worthless as up to now 
everything passes travisCI. So hopefully now we can latch the test framework 
properly into travisCI so future development is properly controlled.  

This is something we're looking at [principles](https://blog.parasoft.com/start-to-love-spring-testing-with-unit-test-assistant-for-java)

### Upgrade to spring-boot 2

We decided that now was probably a good time to upgrade to spring-boot 2. By
doing this we move the project to a base with better long term support and we
gain access to spring-security 5, which is a major improvement.  Native JWT 
support is included in spring-security 5. 

So far the upgrade worked. There were a few issues. This leaves the codebase 
without any security implementation. So the current builds are starting 
nikita but it is not possible to log on until we get security working.

## 2018-02-23

 - OData support

### OData support
We finally managed to get some OData support into the codebase. We are using
antlr4 and are developing our own OData description in antlr4. This file can 
be found in the odata directory under resources and this is where all future 
OData handling will be derived from. 

An example is to be able to take 

     `startsWith(tittel,'hello')`

and it gets turned into a 

`select * from fonds where ownedBy ='admin' and title LIKE 'hello%'`

We're seeing a mismatch between column names (owned_by) and variable names 
(ownedBy). HQL/ES require object names, while SQL requires column names. We 
started to deal with this but it's going to be a tedious job!

I'm still not sure how to deal with HQL. HQL looks like it wants to use a 
Query object as I guess SQL is not an issue then. But it also supports a SQL 
like syntax that can be used. But using Query means taking the database 
closer into the Parser requiring a CriteriaBuilder. It just seems messy! 

We added a file called odata_samples.txt and a simple java application called
TestODataApp under nikita.webapp.run. We also added our own 
walker currently called NikitaODataWalker. There are subclasses of this class
that take care of the actual conversion from OData filter syntax to SQL/HQL 
as well as Elasticsearch query JSON. 

I've decided to invest a little time on OData to HQL/SQL/ES as I think the 
code is applicable for use by others. I've seen a good few requests for this 
on the Internet but no solution other than Apache Olingo, but that requires 
you to use their stack. We should also considering making this part of the 
code available under a BSD-style license.  

Once this code matures a little, it will be used at the service level in 
order to filter the results of incoming requests. But we are not there yet. I
think we need develop a solution and think about it. I see e.g that we are 
parsing the [contextPath][api] portion of a OData URL, when we just really want
to take the command part.


## 2018-02-16

 - OData support
 - Minor additions
 
### OData support
We finally started looking at OData and it might not be as bad as initially 
feared.  I've been playing with antlr4 trying to figure it out, watched some 
tutorials, downloaded resources etc. I managed to get a parser that can 
handle some of the OData syntax working.

Surprisingly I [found](https://tools.oasis-open.org/version-control/browse/wsvn/odata/trunk/spec/grammar/ANTLR/ODataParser.g4)  
an antlr4 (.g4) file hosted by OASIS that can't be parsed by antlr. Apparently
this file has some problems and I believe a SO flow post said it likely had 
automatically been translated from another format.  It's worrying if an 
organisation like oasis open publishes incorrect standards. Unfortunately I 
do not have time to follow up on this issue.

So that parser hasn't been introduced to the code base yet. I need to see how
I dig into the event based listener and start to do some work on translating
the OData syntax to HQL/SQL. I think it might be a good idea to convert to HQL
as hibernate has support for elastic search and then we could hopefully also 
use elastic search without too many coding additions.

If we do manage to get a decent OData2SQL/HQL/Elastic Search notation, we 
should consider releasing it as an own library. I am seeing some people asking
for this on SO so it's obvious I am not alone in looking for this 
functionality. 
 
### Minor additions
To keep the commit rate up, that we constantly are developing even in slow 
weeks, I've added a few metadata entities. There are about 40 metadata 
entities that need to be introduced and it's very much a boring boiler plate 
job.  

## 2018-02-09

 - Architectural issues

### Architectural issues
We continued work on[issue](https://gitlab
.com/OsloMet-ABI/nikita-noark5-core/issues/105) and met some design issues. 
Should HTTPRequest be forward to the Service layer* Ideally it should not, but
the way we are dealing with Hateaos links (being generated on a per user, 
per authorities basis) meant that we had to consider it. Previously we were 
basing the outgoing Hateoas links on the incoming HTTP request. This was to 
easily migrate nikita to various hosts. As long as you had the correct 
starting off address, the client could tell you what you should be creating 
as the address and contextPath. However, you **NEVER TRUST THE CLIENT!**. You
really don't! We are also using JWT without any connection to the IP address,
so we were left wondering if there was a possible attack vector here where 
someone could hijack a token in someway and cause some trouble. Initially I 
don't think so, but we were left with the thought of why bother opening for a
potential issue. Just lock the outgoing hateaos link addresses down to what 
the server wants them to be. 

The downside is that this limits nikita to 1 instance per hostname. Currently
nikita is behind a virtual hostname in apache along with the actual hostname,
but outgoing Hateaos links are set to nikita.hioa.no. 
 
So we are making the decision that nikita is limited to 1 instance per 
hostname so we can move forward with one given architecture. However, it 
probably is not very difficult to make multiple hostname supported by a single 
nikita instance. But we are not going to code something, adding complexity, 
breaking a clear line behind controller and service layer for something that 
may be required in the future.

## 2018-02-02

 - Coverity issues
 - Clean codebase

### Coverity issues
We wrapped up nearly all issues in Coverity. The remaining issues should fall
away when we update the latest version of spring. Coverity is reporting
CSRF issues, but I need to think a little more about that. We won't spend more 
time on Coverity now, but try to deal with issues as soon as they are reported.
The codebase has shrunk a little as a result of this work.
  
### Clean codebase
Hateaos handling was looked at and there were a number of approaches that were
attempted. This work is described in [issue](https://gitlab
.com/OsloMet-ABI/nikita-noark5-core/issues/105) and consumed most of the time. As
a result nikita should be a little faster and use less memory. 



## 2018-01-26

 - Coverity issues
 - Clean codebase
 - Project management

### Coverity issues
We have started dealing with the backlog of coverity issues. A lot of these 
are minor issues, that I have never really reflected over. While I find some of
them as not being a problem within the codebase, others might reuse parts of
the codebase and then they could be an issue
  
### Clean codebase
We did some minor work on continuing cleaning the codebase, but mostly 
through dealing with Coverity issues  
 
### Project management 
We spent some time looking at travisCI and trying to figure out how this is 
setup. Also looking to see if we can get the Coverity scans to be included as 
part of travisCI. Need to dig a little more. We have completed a mdl course 
on udemy and expect to start improving the GUI   
 
### Going forward 
Focus should be on wrapping up Coverity issues. We should start looking at 
upgrading the project to the latest Spring version. With this JWT will have 
in-built support and we can remove JWT from the code-base. I'd also like to
start on [SAML](https://www.jasha.eu/blogposts/2015/10/saml-authentication-angularjs-spring-security.html) 
integration. SAML was something we tried early on in the project but I was not
able to get SAML/Feide to work with nikita. Hopefully with a little more 
digging and better understanding of spring we will figure it out.

The project also has a lot of warnings reported in Idea. Once the coverity 
issues are dealt with, we should work at removing warnings and perhaps run Ideas
 own bug testing. 
 
An important [issue](https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/105) 
added this week is to do a rework of the hateoas handler. I think there is an
extra comma appearing in output at the end that makes the JSON not valid and
there may be a mismatch between collection types so that we are copying data 
to new objects when wrapping database results in Hateoas.
 
Long term, we need to get the testing framework moving. Even if it's just a 
single running test.
 

## 2018-01-19
 - Clean code
 - Better approach to profiles
 - Going forward

### Clean codebase
We have started cleaning up the code base to remove redundant code. The code originally came
for a jhipster example and has evolved. One of the things we saw was that there was no need 
for own classes for DataSource configuration. Leave configuration in application-*.yml files.
This also resulted in a better approach to profiles.

### Profiles 
We had dev, prod, test and demo profiles. These were unnecessary and potentialy could lead to 
undetected bugs in production. ccscanf recommended not to use profiles in this manner. So we
have changed our approach to profiles and limit profiles to database choice and whether or not
to switch on swagger.   

### Going forward
We are using Coverity and there are a lot if issues being reported in Coverity. So we need to 
stop adding new code and try to fix the backlog of Coverity issues. 

We need to make the development process more TDD. Previously we had a half hearted approach to 
this where pere kept his compliance testing tool ahead of nikita development. We need nikita 
to drive this forward as well. Testing does seem to be complex in spring. The fact that the 
project is a multi module project might be complicating the issue.

OData support is going to be make or break for nikita. We will try and add support via a 
odata2sql bridge and see if we can resolve it that way. Various implementations of OData
exist, including a javascript version. But I wonder how close they are to the standard.   

The other part of the project we will focus on is GUI. We have bought some udemy courses
on material design and angular. Hopefully this will help us shine the code a little
better after a while. 

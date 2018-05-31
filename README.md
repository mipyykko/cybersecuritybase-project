### cybersecuritybase-project

This is a course project for the Cyber Security Base course. The assignment was to implement an web application containing at least 5 of the [OWASP top 10](https://www.owasp.org/index.php/Top_10_2013-Top_10) web security design flaws. 

## The intended functionality

The application is provides a simple signup service to an event. User enters his/her personal information and gets a notification of a successful signup. The administrator can view and delete the signups.

## Installation

1. ``git clone`` the repository
2. Run ``mvn package`` in the application directory

## Usage

1. Run ``java -jar target/cybersecuritybase-project-1.0-SNAPSHOT.jar``
2. The server is now running and application available at ``http://localhost:8080``

## Security flaws

The application is rather insecure and has several security flaws. I've chosen to document the following ones, but it probably contains a lot more. 

### A3: Cross-Site Scripting (XSS)

##### Steps to reproduce problem:

1. Start the server
2. Use Firefox browser version 60 to go to ``http://localhost:8080``
2. Enter ``<script>alert('Boo!')</script>`` as your name
3. Press **Submit** 
4. a) A popup window is shown.
    b) Also, a popup window is shown the Admin page at ``http://localhost.8080/admin``

##### Issues: 

Potentially malicious code can be executed on a victim's browser, leading to potential security issues. In this case, the issue is more of a nuisance -- a script opening a simple alert window -- but potentially much more serious attacks could be launched. 

##### Fix:

A simple way to fix the problem is to substitute all instances of ``th:utext`` with ``th:text`` in both ``done.html`` and ``admin.html``. The input should also be sanitized; after all, it is quite unlikely that someone's name and/or address will contain HTML tags. 
Note that, for example, newer versions of the Chrome browser will catch this attempt and fails to show the page with the potentially malicious script.

### A6: Sensitive data exposure and A7: Missing function level access control

##### Steps to reproduce problem:

1. Start the server
2. Go to ``http://localhost:8080``
3. Enter name and address
4. Press **Submit**
5. Go to ``http://localhost:8080/admin``
6. The names and addresses of registered users are shown

##### Issues:

No function level access control is provided, so user without proper credentials can access potentially sensitive information; in this case names and addresses of the registrants. Also, the administrator view is available in a rather easily guessable location.

##### Fix:

An immediate fix is to disable access to the admin page altogether. Proper authentication and different user right levels should be added to the application, thus restricting the access to sensitive data. Currently the application does not have any kind of authentication. The URL to the administrator view could also be changed. 

### A8: Cross-Site Request Forgery (CSRF) and A5: Security misconfiguration

##### Steps to reproduce problem:

1. Start the server
2. Use Firefox browser version 60 to go to ``http://localhost:8080``
3. Open Web Developer tools by pressing F12 on the keyboard
4. Navigate to Network tab
5. Enter name and address on the signup form
6. Press **Submit**
7. Click on ``form`` in the Developer tools
8. Click ``params`` on the right side window
9. See that no ``_csrf`` token is provided

##### Issues:

The application is configured poorly and cross-site request forgery is disabled in the security configuration. Malicious requests could be forged, for example by creating another web page that submits a form data to this server.

##### Fix:

Enable CSRF in ``SecurityConfiguration.java`` by commenting out or removing the line ``http.csrf().disable();`` Other security configurations are also non-existant, so implementing those should be first priority. 
Note that for example newer Chrome browsers will catch this exploit and stop the user from submitting the form.

### A4: Insecure direct object references and A10: Unvalidated redirects and forwards

##### Steps to reproduce 

1. Start the server
2. Go to ``http://localhost:8080``
3. Enter name and address on the signup form
4. Press **Submit**
5. a) Go to ``http://localhost:8080/admin/delete/1``
6. a) See that registration just entered is not on the list

or alternatively
5. b) Go to ``http://localhost:8080/admin/delete/1?redirect=http://www.google.com``
6. b) You are redirected to Google homepage 

##### Issues:

An attacker can manipulate user data by changing the web site address. In this case signup deletion is handled rather unwisely with a ``GET`` method, and the path to do this is easily guessed. 
Also, the redirection path after the user deletion can be specified directly as a query parameter, thus allowing users to be led to potentially malicious sites. 

##### Fix:

An immediate fix is to disable user deletion in the application. The *very* insecure way of handling deletions by ``GET`` method should at least be changed to a form ``POST`` or ``DELETE`` and, relating to **A7** flaw stated above, some kind of authentication and user rights should be implemented.
Redirection with a visible path in the URL is a very unwise choice. In this case it is also completely unnecessary, and should be avoided altogether -- it's highly unlikely that the administrator user would ever be needed to be redirected to other pages than the admin page after the deletion.



Microservice Atom Sample
==================

This is a sample to show how the
[Atom](https://validator.w3.org/feed/docs/atom.html) protocol can be
used for the communication between microservices. Atom is originally
designed to let users subscribe to feeds like blogs. Any new blog post
is published in the feed. However, this approach can be used for data
such as order information, too.

The project creates Docker containers.

It uses three microservices:
- Order to create orders. This services exposes an Atom feed with all
orders.
- Shipment polls the feed every thirty seconds and extract the
  information needed to ship the items.
- Invoicing polls the feed, too. It extracts all information to send
  out an invoice.

Technologies
------------

- Spring Boot
- Rome to produce and parse Atom feeds.
- Docker Compose to link the containers.

How To Run
----------

The demo can be run with
[Docker Machine and Docker Compose](docker/README.md).

Once you create an order in the order application, after a while the
invoice and the shipment should be shown in the other applications.

Remarks on the Code
-------------------

The microservices are: 
- microservice-atom-order to create the orders
- microserivce-demo-shipping for the shipping
- microservice-demo-invoicing for the invoices


The microservices have an Java main application in `src/test/java` to
run them stand alone. microservice-demo-shipping and
microservice-demo-invoicing both use a stub for the
other order service.

The data of an order is copied - including the data of the customer
and the items. So if a customer or item changes in the order system
this does not influence existing shipments and invoices. It would be
odd if a change to a price would also change existing invoices. Also
only the information needed for the shipment and the invoice are
copied over to the other systems.

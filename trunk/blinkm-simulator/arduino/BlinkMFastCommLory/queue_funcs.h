#ifndef QUEUE_FUNCS_H
#define QUEUE_FUNCS_H

struct element_t {
	byte addr;
	byte* payload;
	byte len;
	struct element_t* next;
};

struct queue_t {
	struct element_t* head;
	struct element_t* tail;
};


static int push(struct queue_t *q, int addr, byte* payload, int len) {

	// malloc 1 lorenzo 0
	//struct element_t* el = (struct element_t*) malloc(sizeof(element_t));
	struct element_t* el = malloc(sizeof *el);

	// check malloc
	if ( el == NULL )
		return -1;

	// init next pointer  
	el->next = NULL;

	if ( q->head == NULL )
		q->head = el;	// first element
	else
		q->tail->next = el;  // attach el to queue

	el->addr = addr;
	el->payload = payload;
	el->len = len;

	q->tail = el;  // end of queue points to this el
	return 0;
}

/* does not free() */
static struct element_t* pop(struct queue_t *q) {
	struct element_t *el = q->head;
	if ( el != NULL )
		q->head = el->next;
	return el;
}

#endif

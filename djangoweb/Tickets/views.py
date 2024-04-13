from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.conf import settings
from .models import Ticket
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

from django.utils import timezone
from decimal import Decimal
from decimal import Decimal, ROUND_DOWN

def tickets(request):
    tickets = Ticket.objects.all()
    return render(request, 'Tickets/tickets.html', {'tickets': tickets})


def c_tickets(request):
    return render(request,'Tickets/comp_tickets.html')


class AddRecord(APIView):
    def post(self, request):
        print(request)
        print(request.data)
        prod_name = request.data.get('prod_name')
        prod_price = request.data.get('prod_price')
        date = request.data.get('date')
        address = request.data.get('address')
        rel = request.data.get('rel')
        # data = request.data.get('results')

        try:
            prod_price = Decimal(prod_price).quantize(Decimal('0.00'), rounding=ROUND_DOWN)
            print(prod_price)
            # date = timezone.datetime.strptime(date, '%Y-%m-%d').date()
        except ValueError:
            return Response({"error": "Invalid date or price format"}, status=status.HTTP_400_BAD_REQUEST)

        ticket = Ticket(
            date=date,
            store_address=address,
            product_name=prod_name,
            price=prod_price,
            social_relevance=rel
        )
        ticket.save()

        return Response({"message": f"Record {prod_name}, {prod_price}, {date}, {address}, {rel}  added successfully"}, status=status.HTTP_200_OK)
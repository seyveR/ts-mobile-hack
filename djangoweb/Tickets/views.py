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

        date = request.data.get('date')
        description = request.data.get('description')
        district = request.data.get('district')
        area = request.data.get('area')
        prod_name = request.data.get('prod_name')
        category = request.data.get('category')
        prod_price = request.data.get('prod_price')
        gramms = request.data.get('gramms')
        price_kg = request.data.get('price_kg')
        price_rost = request.data.get('price_rost')
        rel = request.data.get('rel')
        # data = request.data.get('results')

        try:
            prod_price = Decimal(prod_price).quantize(Decimal('0.00'), rounding=ROUND_DOWN)
            print(prod_price)
            # date = timezone.datetime.strptime(date, '%Y-%m-%d').date()
        except ValueError:
            return Response({"error": "Invalid date or price format"}, status=status.HTTP_400_BAD_REQUEST)
        
        if not price_rost:
            price_rost = "товар не из меморандума"

        ticket = Ticket(
            date=date,
            description=description,
            district=district,
            area=area,
            product_name=prod_name,
            category=category,
            price_tag=prod_price,
            gramms=gramms,
            price_for_kg=price_kg,
            price_rost=price_rost,
            social_relevance=rel
        )
        ticket.save()

        return Response({"message": f"Record {prod_name}, {prod_price}, {date}, {district}, {rel}  added successfully"}, status=status.HTTP_200_OK)
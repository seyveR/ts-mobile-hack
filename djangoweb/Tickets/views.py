from django.shortcuts import render, redirect
from django.http import HttpResponse, JsonResponse
from django.conf import settings
from .models import Ticket, History
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
import ssl

from django.utils import timezone
from decimal import Decimal
from decimal import Decimal, ROUND_DOWN

from django.http import HttpResponseRedirect
import smtplib
from email.mime.text import MIMEText


import logging

logger = logging.getLogger()
logger.setLevel(logging.DEBUG)
logger.addHandler(logging.StreamHandler())

def tickets(request):
    if request.method == 'POST':
        ticket_id = request.POST.get('ticket_id')
        print(ticket_id)
        if ticket_id:
            try:
                ticket = Ticket.objects.get(id=ticket_id)
                history = History(
                    ids=ticket_id,
                    date=ticket.date,
                    district=ticket.district,
                    area=ticket.area,
                    product_name=ticket.product_name,
                    category=ticket.category,
                    price_tag=ticket.price_tag,
                    gramms=ticket.gramms,
                    price_for_kg=ticket.price_for_kg,
                    price_rost=ticket.price_rost
                )
                history.save()
                ticket.delete()
                return redirect('tickets')
            except Ticket.DoesNotExist:
                pass 
    tickets = Ticket.objects.all()
    return render(request, 'Tickets/tickets.html', {'tickets': tickets})



def c_tickets(request):
    history = History.objects.all()
    return render(request,'Tickets/comp_tickets.html', {'history': history})


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
        
        message = f"Поступил запрос от пользователя с товаром «{prod_name}» <br>Цена на который равна {prod_price} рублей <br>Выяснилось, что цена на товар завышена в магазине по следующему адресу: {area} {district}. <br>Более подробная информация на <a href='http://127.0.0.1:8080/'>сайте</a>."

        self.send_email(message)
        confirmation_subject = "Контролируем цены вместе"
        confirmation_message = "Поступил запрос от гражданина"
        email = "seyverrrr@yandex.ru"
        # self.send_email_confirm(confirmation_subject, confirmation_message, email)
        

        return Response({"message": f"Record {prod_name}, {prod_price}, {date}, {district}, {rel}  added successfully"}, status=status.HTTP_200_OK)
    


    def send_email(self, message):
        sender = 'seyveRRRR@yandex.ru'
        password = 'lkjpjfzitguqhxrp'

        try:      
            server = smtplib.SMTP("smtp.yandex.ru", 587)
            context = ssl.create_default_context()
            server.starttls(context=context)
            logger.debug(server.login(sender, password))
            msg = MIMEText(message, 'html', 'utf-8')
            msg['From'] = sender
            msg['To'] = 'akvopf@mail.ru'
            msg['Subject'] = "Контролируем цены"

            server.sendmail(sender, 'akvopf@mail.ru', msg.as_string())
        except Exception as _ex:
            print(f"{_ex}\nCheck ur login or pass")
        finally:
            server.quit()

    def send_email_confirm(self, subject, message, email):
        sender = 'seyveRRRR@yandex.ru'
        password = 'lkjpjfzitguqhxrp'
        server = smtplib.SMTP("smtp.yandex.ru", 587)
        server.starttls()

        try:
            server.login(sender, password)
            msg = MIMEText(message, 'plain', 'utf-8')
            msg['From'] = sender
            msg['To'] = email
            msg['Subject'] = subject

            server.sendmail(sender, email, msg.as_string())
        except Exception as _ex:
            return f"{_ex}\nCheck ur login or pass"
        finally:
            server.quit()
        

    # def site(request):
    #     name = "System" # Assuming you want to use a system-generated name
    #     phone = "N/A" # Assuming you want to use a system-generated phone number
    #     email = "seyverrrr@yandex.ru" # Assuming you want to use a system-generated email
    #     message = f"Имя: {name}\nТелефон: {phone}\nПочта: {email}"
    #     send_email(message) 
    #     confirmation_subject = "Обращение в Простор"
    #     confirmation_message = "Ваше обращение зарегистрировано, ожидайте обратного звонка"
    #     send_email_confirm(confirmation_subject, confirmation_message, email)
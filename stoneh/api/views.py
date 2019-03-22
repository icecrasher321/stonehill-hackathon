from django.shortcuts import render, redirect
from django.db.models import Q
from .models import *
import django_filters.rest_framework
from django_filters import NumberFilter
from rest_framework.authentication import SessionAuthentication, BasicAuthentication
from rest_framework import generics, mixins
from rest_framework.permissions import IsAuthenticated
from .forms import *
from .serializers import *
# Create your views here.
class DishPrefAPIView(mixins.UpdateModelMixin, generics.ListCreateAPIView): # DetailView CreateView FormView
    #lookup_field            = 'pk' # slug, id # url(r'?P<pk>\d+')
    serializer_class        = DishPrefSerializer


    def put(self, request, *args, **kwargs):
        return self.update(request, *args, **kwargs)

    def get_queryset(self):
        return DishPref.objects.all()

class UserAPIView(mixins.UpdateModelMixin, generics.ListCreateAPIView):

    model = User
    queryset = User.objects.all()
    def put(self, request, *args, **kwargs):
        return self.partial_update(request, *args, **kwargs)

    serializer_class = UserSerializer


class UserUpdateAPIView(generics.RetrieveUpdateAPIView):


    queryset = User.objects.all()
    serializer_class = UserUpdateSerializer

    lookup_field = 'pk'

    def get_serializer_context(self, *args, **kwargs):
        return {"request": self.request}

    def put(self, request, *args, **kwargs):
        return self.update(request, *args, **kwargs)



def restform(request):

    if request.method == "POST":
        form = RestaurantForm(request.POST)
        if form.is_valid():
            k = form.save(commit = False)
            k.user = request.user
            k.save()
            return redirect('http://127.0.0.1:8000/dishform')

    else:
        form = RestaurantForm()

    return render(request, 'restaurant.html', {'form': form})

def dishform(request):

    if request.method == "POST":
        form = DishForm(request.POST)
        if form.is_valid():
            a = form.save(commit = False)
            a.restaurant = Restaurant.objects.get(user=request.user)
            a.save()
        return redirect('http://127.0.0.1:8000/register')
    else:
        form = DishForm()

    return render(request, 'dish.html', {'form': form})

def register(request):
    if request.method == 'POST':
        form = UserInfoForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('login')
    else:
        form = UserInfoForm()

    return render(request, 'registration/register.html', {'form': form})

def get_dish(request):
    pass

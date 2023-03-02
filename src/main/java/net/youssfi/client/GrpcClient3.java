package net.youssfi.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.youssfi.stubs.BankServiceGrpc;
import net.youssfi.stubs.Ebank;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GrpcClient3 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",9999)
                .usePlaintext()
                .build();

        BankServiceGrpc.BankServiceStub asyncStub=BankServiceGrpc.newStub(managedChannel);

        Ebank.ConvertCurrencyRequest request= Ebank.ConvertCurrencyRequest.newBuilder()
                .setAmount(5000)
                .build();
        asyncStub.getStream(request, new StreamObserver<Ebank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Ebank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("================");
                System.out.println(convertCurrencyResponse);
                System.out.println("================");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("END");
            }
        });

        System.in.read();
    }
}
